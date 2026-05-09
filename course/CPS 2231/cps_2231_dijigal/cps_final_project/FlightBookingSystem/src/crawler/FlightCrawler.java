package crawler;

import com.microsoft.playwright.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlightCrawler {

    // Configuration
    private static final int THREAD_COUNT = 4;
    private static final String PATH_TO_CSV_FILE = "C:\\Users\\Administrator\\Desktop\\mypage\\flight_data_database.csv";

    // Persistent Playwright instances
    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;

    // Regex 1: 用于分离航空公司、航班号和机型 (e.g., 中国联合航空KN5988?波音737(中) -> Group1: 中国联合航空,
    // Group2: KN5988, Group3: 波音737(中))
    private static final Pattern AIRLINE_INFO_PATTERN = Pattern.compile("^(.*?)([A-Za-z0-9]+)\\?(.*)$");
    // Regex 2: 用于从URL中提取三字码和日期 (e.g., oneway-sha-hjj?depdate=2025-12-03)
    private static final Pattern URL_ROUTE_PATTERN = Pattern
            .compile("/oneway-(\\w{3})-(\\w{3})\\?depdate=(\\d{4}-\\d{2}-\\d{2})");

    public static void main(String[] args) {
        // 1. Generate URLs internally
        List<String> urls = FlightRouteGenerator.generateUrls();

        // 3. Setup Playwright
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));

            // 4. Create Thread Pool
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

            for (String url : urls) {
                executor.submit(() -> processUrl(browser, url));
            }

            // 5. Shutdown Logic
            try {
                executor.awaitTermination(24, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Crawling complete.");
    }

    private static void processUrl(Browser browser, String url) {
        // --- 1. Extract Route/Date from URL ---
        Matcher matcher = URL_ROUTE_PATTERN.matcher(url);
        String originCode = "N/A";
        String destinationCode = "N/A";
        String date = "N/A";

        if (matcher.find()) {
            originCode = matcher.group(1).toUpperCase();
            destinationCode = matcher.group(2).toUpperCase();
            date = matcher.group(3);
        }

        // --- 2. Start Playwright Process ---
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setUserAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .setViewportSize(1920, 1080));

        Page page = context.newPage();

        try {
            System.out.println("Processing: " + url);
            page.navigate(url);

            handlePopups(page);

            // CRITICAL: Wait for the flight list container to appear
            try {
                page.waitForSelector(".flight-box", new Page.WaitForSelectorOptions().setTimeout(30000));
            } catch (TimeoutError e) {
                System.err.println("Timeout: Flight list did not appear for " + originCode + "-" + destinationCode
                        + ". Check Captcha/Anti-Bot.");
            }

            autoScroll(page);

            List<ElementHandle> flightBoxes = page.querySelectorAll(".flight-box");

            if (flightBoxes.isEmpty()) {
                System.out.println("No flights found for: " + originCode + "-" + destinationCode + " on " + date);
            }

        } catch (Exception e) {
            System.err.println("Error processing " + url + ": " + e.getMessage());
        } finally {
            context.close();
        }
    }

    /**
     * Search for flights based on user criteria.
     * This method is called by the JavaFX application.
     */
    public static List<FlightInfo> searchFlights(String origin, String destination, String date) {
        List<FlightInfo> results = new java.util.ArrayList<>();

        // 1. Resolve Codes
        String originCode = CityCodeMapper.getCityCode(origin);
        String destCode = CityCodeMapper.getCityCode(destination);

        if (originCode == null || destCode == null) {
            System.err.println("Could not resolve  codes for: " + origin + " -> " + destination);
            return results;
        }

        // 2. Construct URL
        String url = String.format(
                "https://flights.ctrip.com/online/list/oneway-%s-%s?depdate=%s&cabin=y&adult=1&child=0&infant=0",
                originCode, destCode, date);

        System.out.println("Searching URL: " + url);

        // 3. Launch Browser & Scrape (Persistent)
        try {
            // Initialize Playwright/Browser if not already active
            if (playwright == null) {
                playwright = Playwright.create();
            }
            if (browser == null || !browser.isConnected()) {
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                        .setHeadless(false)); // Show browser as requested
            }
            if (context == null) {
                context = browser.newContext(new Browser.NewContextOptions()
                        .setUserAgent(
                                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .setViewportSize(1920, 1080));
            }

            Page page = context.newPage();

            page.navigate(url);
            handlePopups(page);

            // Wait for results
            try {
                page.waitForSelector(".flight-box", new Page.WaitForSelectorOptions().setTimeout(15000));
            } catch (TimeoutError e) {
                System.err.println("Timeout waiting for flight list.");
            }

            autoScroll(page);

            List<ElementHandle> flightBoxes = page.querySelectorAll(".flight-box");
            System.out.println("Found " + flightBoxes.size() + " flights.");

            for (ElementHandle box : flightBoxes) {
                FlightInfo info = extractFlightData(box, origin, destination, date);
                if (info != null) {
                    results.add(info);
                }
            }

            // Note: Browser and Page remain open as requested.

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    private static void handlePopups(Page page) {
        try {
            Locator closeBtn = page.locator("text=阅读并同意携程的服务协议");
            if (closeBtn.isVisible()) {
                page.keyboard().press("Escape");
            }
        } catch (Exception ignored) {
        }
    }

    private static void autoScroll(Page page) {
        // **FIXED CASTING ISSUE HERE**: Ensure safe conversion from Playwright's
        // Integer result to long.
        long previousHeight = 0;
        long currentHeight = 0;
        int attempts = 0;

        while (attempts < 5) {
            Object result = page.evaluate("document.body.scrollHeight");
            if (result instanceof Number) {
                previousHeight = ((Number) result).longValue();
            } else {
                previousHeight = 0;
            }

            page.mouse().wheel(0, 1000);
            page.waitForTimeout(1000);

            result = page.evaluate("document.body.scrollHeight");
            if (result instanceof Number) {
                currentHeight = ((Number) result).longValue();
            } else {
                currentHeight = 0;
            }

            if (currentHeight == previousHeight) {
                attempts++;
            } else {
                attempts = 0;
            }
        }
    }

    private static FlightInfo extractFlightData(ElementHandle box, String originCode, String destinationCode,
            String date) {
        try {
            // Raw text extraction
            String rawAirlineInfo = safeText(box.querySelector(".airline-name"));
            String depAirport = safeText(box.querySelector(".depart-box .airport"));
            String arrAirport = safeText(box.querySelector(".arrive-box .airport"));
            String depTime = safeText(box.querySelector(".depart-box .time"));
            String rawArrivalTime = safeText(box.querySelector(".arrive-box .time"));
            String rawPrice = safeText(box.querySelector(".price"));

            // --- 1. Clean Airline Info ---
            String airlineName = rawAirlineInfo;
            String flightNumber = "N/A";
            String aircraftType = "N/A";

            // Apply Regex to split the information
            Matcher infoMatcher = AIRLINE_INFO_PATTERN.matcher(rawAirlineInfo);
            if (infoMatcher.find()) {
                airlineName = infoMatcher.group(1).trim();
                flightNumber = infoMatcher.group(2).trim();
                aircraftType = infoMatcher.group(3).trim();
            }

            // --- 2. Clean Arrival Time (Remove \n+X天 where X is any number) ---
            // This handles +1天, +2天, +3天, etc. and also handles newlines
            String cleanArrivalTime = rawArrivalTime.replaceAll("\\s*\\+\\d+天", "").trim();

            // --- 3. Clean Price (Remove ? and 起) ---
            String cleanPrice = rawPrice.replaceAll("[?起\\s]", "").trim();

            return new FlightInfo(
                    airlineName, flightNumber, aircraftType,
                    depAirport, arrAirport, depTime, cleanArrivalTime,
                    cleanPrice, originCode, destinationCode, date);
        } catch (Exception e) {
            System.err.println("Error extracting individual flight: " + e.getMessage());
            return null;
        }
    }

    public static void closeBrowser() {
        if (context != null) {
            context.close();
            context = null;
        }
        if (browser != null) {
            browser.close();
            browser = null;
        }
        if (playwright != null) {
            playwright.close();
            playwright = null;
        }
    }

    private static String safeText(ElementHandle element) {
        return element == null ? "N/A" : element.innerText().trim();
    }

    // Updated FlightInfo class for clearer data separation (all String type)
    public static class FlightInfo {
        private String airline;
        private String flightNumber;
        private String aircraftType;
        private String departureAirport;
        private String arrivalAirport;
        private String departureTime;
        private String arrivalTime;
        private String price;
        private String originCode;
        private String destinationCode;
        private String date;

        public FlightInfo(String airline, String flightNumber, String aircraftType, String departureAirport,
                String arrivalAirport,
                String departureTime, String arrivalTime, String price, String originCode, String destinationCode,
                String date) {
            this.airline = airline;
            this.flightNumber = flightNumber;
            this.aircraftType = aircraftType;
            this.departureAirport = departureAirport;
            this.arrivalAirport = arrivalAirport;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.price = price;
            this.originCode = originCode;
            this.destinationCode = destinationCode;
            this.date = date;
        }

        // Getters
        public String getAirline() {
            return airline;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        public String getAircraftType() {
            return aircraftType;
        }

        public String getDepartureAirport() {
            return departureAirport;
        }

        public String getArrivalAirport() {
            return arrivalAirport;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public String getPrice() {
            return price;
        }

        public String getOriginCode() {
            return originCode;
        }

        public String getDestinationCode() {
            return destinationCode;
        }

        public String getDate() {
            return date;
        }

        @Override
        public String toString() {
            return String.format("%s (%s) | %s - %s | %s -> %s | ¥%s",
                    airline, flightNumber, departureTime, arrivalTime, departureAirport, arrivalAirport, price);
        }
    }
}