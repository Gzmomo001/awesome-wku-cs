package crawler;

import java.util.HashMap;
import java.util.Map;

public class CityCodeMapper {

    // Map for Domestic (China) Locations
    private static final Map<String, String> DOMESTIC_MAPPINGS = new HashMap<>();
    // Map for International Locations
    private static final Map<String, String> INTERNATIONAL_MAPPINGS = new HashMap<>();

    static {
        // --- Domestic Mappings (Based on your provided list) ---
        // Major Hubs
        DOMESTIC_MAPPINGS.put("bjs", "Beijing");
        DOMESTIC_MAPPINGS.put("sha", "Shanghai");
        DOMESTIC_MAPPINGS.put("tsn", "Tianjin");
        DOMESTIC_MAPPINGS.put("ckg", "Chongqing");
        DOMESTIC_MAPPINGS.put("can", "Guangzhou");
        DOMESTIC_MAPPINGS.put("szx", "Shenzhen");
        DOMESTIC_MAPPINGS.put("ctu", "Chengdu");
        DOMESTIC_MAPPINGS.put("hgh", "Hangzhou");
        DOMESTIC_MAPPINGS.put("xmn", "Xiamen");
        DOMESTIC_MAPPINGS.put("nkg", "Nanjing");
        DOMESTIC_MAPPINGS.put("wuh", "Wuhan");
        DOMESTIC_MAPPINGS.put("csx", "Changsha");
        DOMESTIC_MAPPINGS.put("kwe", "Guiyang");
        DOMESTIC_MAPPINGS.put("kmg", "Kunming");
        DOMESTIC_MAPPINGS.put("xi'an", "Xi'an"); // Handling standard naming if needed
        DOMESTIC_MAPPINGS.put("sia", "Xi'an"); // Standard Code for Xi'an

        // North China
        DOMESTIC_MAPPINGS.put("sjw", "Shijiazhuang");
        DOMESTIC_MAPPINGS.put("shp", "Qinhuangdao");
        DOMESTIC_MAPPINGS.put("shf", "Shanhaiguan");
        DOMESTIC_MAPPINGS.put("tyn", "Taiyuan");
        DOMESTIC_MAPPINGS.put("dat", "Datong");
        DOMESTIC_MAPPINGS.put("cih", "Changzhi");
        DOMESTIC_MAPPINGS.put("het", "Hohhot");
        DOMESTIC_MAPPINGS.put("bav", "Baotou");
        DOMESTIC_MAPPINGS.put("xil", "Xilinhot");
        DOMESTIC_MAPPINGS.put("hlh", "Ulanhot");
        DOMESTIC_MAPPINGS.put("hld", "Hulunbuir (Hailar)");
        DOMESTIC_MAPPINGS.put("wua", "Wuhai");
        DOMESTIC_MAPPINGS.put("cif", "Chifeng");
        DOMESTIC_MAPPINGS.put("tgo", "Tongliao");
        DOMESTIC_MAPPINGS.put("nzh", "Manzhouli");

        // Northeast China
        DOMESTIC_MAPPINGS.put("hrb", "Harbin");
        DOMESTIC_MAPPINGS.put("ndg", "Qiqihar");
        DOMESTIC_MAPPINGS.put("mdg", "Mudanjiang");
        DOMESTIC_MAPPINGS.put("jmu", "Jiamusi");
        DOMESTIC_MAPPINGS.put("hek", "Heihe");
        DOMESTIC_MAPPINGS.put("cgq", "Changchun");
        DOMESTIC_MAPPINGS.put("jil", "Jilin");
        DOMESTIC_MAPPINGS.put("ynj", "Yanji");
        DOMESTIC_MAPPINGS.put("she", "Shenyang");
        DOMESTIC_MAPPINGS.put("dlc", "Dalian");
        DOMESTIC_MAPPINGS.put("ddg", "Dandong");
        DOMESTIC_MAPPINGS.put("chg", "Chaoyang");
        // DOMESTIC_MAPPINGS.put("iob", "Unknown (IOB)"); // Code included in list but
        // rare/non-standard

        // East China
        DOMESTIC_MAPPINGS.put("lyg", "Lianyungang");
        DOMESTIC_MAPPINGS.put("ntg", "Nantong");
        DOMESTIC_MAPPINGS.put("czx", "Changzhou");
        DOMESTIC_MAPPINGS.put("xuz", "Xuzhou");
        DOMESTIC_MAPPINGS.put("ynz", "Yancheng");
        DOMESTIC_MAPPINGS.put("wux", "Wuxi");
        DOMESTIC_MAPPINGS.put("foc", "Fuzhou");
        DOMESTIC_MAPPINGS.put("wus", "Wuyishan");
        DOMESTIC_MAPPINGS.put("jjn", "Quanzhou");
        DOMESTIC_MAPPINGS.put("kow", "Ganzhou");
        DOMESTIC_MAPPINGS.put("wnz", "Wenzhou");
        DOMESTIC_MAPPINGS.put("ngb", "Ningbo");
        DOMESTIC_MAPPINGS.put("yiw", "Yiwu");
        DOMESTIC_MAPPINGS.put("hsn", "Zhoushan");
        DOMESTIC_MAPPINGS.put("juz", "Quzhou");
        DOMESTIC_MAPPINGS.put("hyn", "Taizhou");
        DOMESTIC_MAPPINGS.put("khn", "Nanchang");
        DOMESTIC_MAPPINGS.put("jdz", "Jingdezhen");
        DOMESTIC_MAPPINGS.put("jiu", "Jiujiang");
        DOMESTIC_MAPPINGS.put("jgs", "Jinggangshan");
        DOMESTIC_MAPPINGS.put("tna", "Jinan");
        DOMESTIC_MAPPINGS.put("weh", "Weihai");
        DOMESTIC_MAPPINGS.put("tao", "Qingdao");
        DOMESTIC_MAPPINGS.put("ynt", "Yantai");
        DOMESTIC_MAPPINGS.put("jng", "Jining");
        DOMESTIC_MAPPINGS.put("wef", "Weifang");
        DOMESTIC_MAPPINGS.put("doy", "Dongying");
        DOMESTIC_MAPPINGS.put("lyi", "Linyi");
        DOMESTIC_MAPPINGS.put("hfe", "Hefei");
        DOMESTIC_MAPPINGS.put("txn", "Huangshan");
        DOMESTIC_MAPPINGS.put("fug", "Fuyang");
        DOMESTIC_MAPPINGS.put("aqg", "Anqing");

        // Central/South China
        DOMESTIC_MAPPINGS.put("cgo", "Zhengzhou");
        DOMESTIC_MAPPINGS.put("nny", "Nanyang");
        DOMESTIC_MAPPINGS.put("lya", "Luoyang");
        DOMESTIC_MAPPINGS.put("ayn", "Anyang");
        DOMESTIC_MAPPINGS.put("dyg", "Zhangjiajie");
        DOMESTIC_MAPPINGS.put("cgd", "Changde");
        DOMESTIC_MAPPINGS.put("hny", "Hengyang");
        DOMESTIC_MAPPINGS.put("hjj", "Huaihua");
        DOMESTIC_MAPPINGS.put("llf", "Yongzhou");
        DOMESTIC_MAPPINGS.put("yih", "Yichang");
        DOMESTIC_MAPPINGS.put("xfn", "Xiangyang");
        DOMESTIC_MAPPINGS.put("shs", "Shashi");
        DOMESTIC_MAPPINGS.put("enh", "Enshi");
        DOMESTIC_MAPPINGS.put("zuh", "Zhuhai");
        DOMESTIC_MAPPINGS.put("swa", "Jieyang");
        DOMESTIC_MAPPINGS.put("mxz", "Meizhou");
        DOMESTIC_MAPPINGS.put("zha", "Zhanjiang");
        DOMESTIC_MAPPINGS.put("xin", "Xingning");
        DOMESTIC_MAPPINGS.put("nng", "Nanning");
        DOMESTIC_MAPPINGS.put("kwl", "Guilin");
        DOMESTIC_MAPPINGS.put("lzh", "Liuzhou");
        DOMESTIC_MAPPINGS.put("wuz", "Wuzhou");
        DOMESTIC_MAPPINGS.put("bhy", "Beihai");
        DOMESTIC_MAPPINGS.put("hak", "Haikou");
        DOMESTIC_MAPPINGS.put("syx", "Sanya");

        // Southwest China
        DOMESTIC_MAPPINGS.put("lzo", "Luzhou");
        DOMESTIC_MAPPINGS.put("ybp", "Yibin");
        DOMESTIC_MAPPINGS.put("mig", "Mianyang");
        DOMESTIC_MAPPINGS.put("jzh", "Jiuzhaigou");
        DOMESTIC_MAPPINGS.put("pzi", "Panzhihua");
        DOMESTIC_MAPPINGS.put("dax", "Dazhou");
        DOMESTIC_MAPPINGS.put("wxn", "Wanzhou");
        DOMESTIC_MAPPINGS.put("xic", "Xichang");
        DOMESTIC_MAPPINGS.put("nao", "Nanchong");
        DOMESTIC_MAPPINGS.put("gys", "Guangyuan");
        DOMESTIC_MAPPINGS.put("zyi", "Zunyi");
        DOMESTIC_MAPPINGS.put("ava", "Anshun");
        DOMESTIC_MAPPINGS.put("ten", "Tongren");
        DOMESTIC_MAPPINGS.put("ljg", "Lijiang");
        DOMESTIC_MAPPINGS.put("jhg", "Xishuangbanna");
        DOMESTIC_MAPPINGS.put("dlu", "Dali");
        DOMESTIC_MAPPINGS.put("sym", "Pu'er");
        DOMESTIC_MAPPINGS.put("bsd", "Baoshan");
        DOMESTIC_MAPPINGS.put("lnj", "Lincang");
        DOMESTIC_MAPPINGS.put("zat", "Zhaotong");
        DOMESTIC_MAPPINGS.put("yua", "Yuanmou");
        DOMESTIC_MAPPINGS.put("lum", "Mangshi");
        DOMESTIC_MAPPINGS.put("dig", "Shangri-La (Diqing)");
        DOMESTIC_MAPPINGS.put("lxa", "Lhasa");
        DOMESTIC_MAPPINGS.put("bpx", "Qamdo");
        DOMESTIC_MAPPINGS.put("acx", "Xingyi");

        // Northwest China
        DOMESTIC_MAPPINGS.put("urc", "Urumqi");
        DOMESTIC_MAPPINGS.put("khg", "Kashi");
        DOMESTIC_MAPPINGS.put("yin", "Yining");
        DOMESTIC_MAPPINGS.put("krl", "Korla");
        DOMESTIC_MAPPINGS.put("aku", "Aksu");
        DOMESTIC_MAPPINGS.put("htn", "Hotan");
        DOMESTIC_MAPPINGS.put("aat", "Altay");
        DOMESTIC_MAPPINGS.put("hmi", "Hami");
        DOMESTIC_MAPPINGS.put("kry", "Karamay");
        DOMESTIC_MAPPINGS.put("fyn", "Fuyun");
        DOMESTIC_MAPPINGS.put("tcg", "Tacheng");
        DOMESTIC_MAPPINGS.put("kca", "Kuqa");
        DOMESTIC_MAPPINGS.put("iqm", "Qiemo");
        DOMESTIC_MAPPINGS.put("eny", "Yan'an");
        DOMESTIC_MAPPINGS.put("aka", "Ankang");
        DOMESTIC_MAPPINGS.put("uyn", "Yulin");
        DOMESTIC_MAPPINGS.put("hzg", "Hanzhong");
        DOMESTIC_MAPPINGS.put("dnh", "Dunhuang");
        DOMESTIC_MAPPINGS.put("jgn", "Jiayuguan");
        DOMESTIC_MAPPINGS.put("chw", "Jiuquan");
        DOMESTIC_MAPPINGS.put("iqn", "Qingyang");
        DOMESTIC_MAPPINGS.put("lhw", "Lanzhou");
        DOMESTIC_MAPPINGS.put("xnn", "Xining");
        DOMESTIC_MAPPINGS.put("goq", "Golmud");
        DOMESTIC_MAPPINGS.put("inc", "Yinchuan");

        // Special Administrative Regions (Included in user's domestic list)
        DOMESTIC_MAPPINGS.put("hkg", "Hong Kong");
        DOMESTIC_MAPPINGS.put("mfm", "Macau");

        // --- International Mappings (Available International Locations) ---
        // Asia
        INTERNATIONAL_MAPPINGS.put("tyo", "Tokyo");
        INTERNATIONAL_MAPPINGS.put("nrt", "(Narita)");
        INTERNATIONAL_MAPPINGS.put("hnd", "(Haneda)");
        INTERNATIONAL_MAPPINGS.put("sel", "Seoul");
        INTERNATIONAL_MAPPINGS.put("icn", "(Incheon)");
        INTERNATIONAL_MAPPINGS.put("gmp", "(Gimpo)");
        INTERNATIONAL_MAPPINGS.put("osa", "Osaka");
        INTERNATIONAL_MAPPINGS.put("kix", "(Kansai)");
        INTERNATIONAL_MAPPINGS.put("sin", "Singapore");
        INTERNATIONAL_MAPPINGS.put("bkk", "Bangkok");
        INTERNATIONAL_MAPPINGS.put("dmk", "(Don Mueang)");
        INTERNATIONAL_MAPPINGS.put("kul", "Kuala Lumpur");
        INTERNATIONAL_MAPPINGS.put("hkg", "Hong Kong"); // Often treated as Int'l
        INTERNATIONAL_MAPPINGS.put("tpe", "Taipei");
        INTERNATIONAL_MAPPINGS.put("dxb", "Dubai");
        INTERNATIONAL_MAPPINGS.put("doh", "Doha");

        // Europe
        INTERNATIONAL_MAPPINGS.put("lon", "London");
        INTERNATIONAL_MAPPINGS.put("lhr", "(Heathrow)");
        INTERNATIONAL_MAPPINGS.put("lgw", "(Gatwick)");
        INTERNATIONAL_MAPPINGS.put("par", "Paris");
        INTERNATIONAL_MAPPINGS.put("cdg", "(Charles de Gaulle)");
        INTERNATIONAL_MAPPINGS.put("fra", "Frankfurt");
        INTERNATIONAL_MAPPINGS.put("ams", "Amsterdam");
        INTERNATIONAL_MAPPINGS.put("mad", "Madrid");
        INTERNATIONAL_MAPPINGS.put("bcn", "Barcelona");
        INTERNATIONAL_MAPPINGS.put("rom", "Rome");
        INTERNATIONAL_MAPPINGS.put("fco", "(Fiumicino)");
        INTERNATIONAL_MAPPINGS.put("zrh", "Zurich");
        INTERNATIONAL_MAPPINGS.put("mow", "Moscow");
        INTERNATIONAL_MAPPINGS.put("ist", "Istanbul");

        // North America
        INTERNATIONAL_MAPPINGS.put("nyc", "New York");
        INTERNATIONAL_MAPPINGS.put("jfk", "(JFK)");
        INTERNATIONAL_MAPPINGS.put("ewr", "(Newark)");
        INTERNATIONAL_MAPPINGS.put("lax", "Los Angeles");
        INTERNATIONAL_MAPPINGS.put("sfo", "San Francisco");
        INTERNATIONAL_MAPPINGS.put("chi", "Chicago");
        INTERNATIONAL_MAPPINGS.put("ord", "(O'Hare)");
        INTERNATIONAL_MAPPINGS.put("yvr", "Vancouver");
        INTERNATIONAL_MAPPINGS.put("yyz", "Toronto");
        DOMESTIC_MAPPINGS.put("shg", "shungnak");

        // Oceania
        INTERNATIONAL_MAPPINGS.put("syd", "Sydney");
        INTERNATIONAL_MAPPINGS.put("mel", "Melbourne");
        INTERNATIONAL_MAPPINGS.put("akl", "Auckland");
    }

    /**
     * Get the city name for a given airport/city code.
     * Checks domestic list first, then international.
     * 
     * @param code The 3-letter IATA code (case insensitive)
     * @return The city name, or "Unknown Code: [code]" if not found.
     */
    public static String getCityName(String code) {
        if (code == null)
            return "Unknown";
        String lowerCode = code.toLowerCase();

        if (DOMESTIC_MAPPINGS.containsKey(lowerCode)) {
            return DOMESTIC_MAPPINGS.get(lowerCode);
        }
        if (INTERNATIONAL_MAPPINGS.containsKey(lowerCode)) {
            return INTERNATIONAL_MAPPINGS.get(lowerCode);
        }
        return "Unknown Code: " + code;
    }

    /**
     * Check if a code corresponds to a domestic location.
     */
    public static String determineFlightType(String origin, String destination) {
        if (origin != null && destination != null && DOMESTIC_MAPPINGS.containsKey(origin.toLowerCase())
                && DOMESTIC_MAPPINGS.containsKey(destination.toLowerCase())) {
            return "Domestic";
        }
        return "International";
    }

    /**
     * Get the domestic map.
     */
    public static Map<String, String> getDomesticMappings() {
        return new HashMap<>(DOMESTIC_MAPPINGS);
    }

    /**
     * Get the international map.
     */
    public static Map<String, String> getInternationalMappings() {
        return new HashMap<>(INTERNATIONAL_MAPPINGS);
    }

    /**
     * Get the city code for a given city name.
     * Case insensitive search.
     * 
     * @param name The city name (e.g., "Shanghai")
     * @return The 3-letter code (e.g., "sha"), or null if not found.
     */
    public static String getCityCode(String name) {
        // fuzzy mapping

        if (name == null)
            return null;
        String lowerName = name.trim().toLowerCase();

        // Check if the input is already a valid code (length 3)
        if (lowerName.length() == 3) {
            if (DOMESTIC_MAPPINGS.containsKey(lowerName) || INTERNATIONAL_MAPPINGS.containsKey(lowerName)) {
                return lowerName;
            }
        }

        // Search in domestic mappings
        for (Map.Entry<String, String> entry : DOMESTIC_MAPPINGS.entrySet()) {
            if (entry.getValue().toLowerCase().contains(lowerName)) {
                return entry.getKey();
            }
        }

        // Search in international mappings
        for (Map.Entry<String, String> entry : INTERNATIONAL_MAPPINGS.entrySet()) {
            if (entry.getValue().toLowerCase().contains(lowerName)) {
                return entry.getKey();
            }
        }

        return null;
    }
}