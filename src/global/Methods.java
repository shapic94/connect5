package global;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Methods {

    //    public static String getEmptyChildId(String[] vertices) {
//        String[] vertex;
//        boolean first = true;
//        boolean second = true;
//        boolean third = true;
//
//        for (int i = 0; i < vertices.length; i++) {
//            if (vertices[i].contains(".")) {
//                vertex = vertices[i].split(".");
//                if (vertex[vertex.length].equals("0")) {
//                    first = false;
//                } else if (vertex[vertex.length].equals("1")) {
//                    second = false;
//                } else if (vertex[vertex.length].equals("2")) {
//                    third = false;
//                }
//            } else {
//                if (vertices[i].equals("0")) {
//                    first = false;
//                } else if (vertices[i].equals("1")) {
//                    second = false;
//                } else if (vertices[i].equals("2")) {
//                    third = false;
//                }s
//            }
//        }
//
//        if (first) {
//            return "0";
//        } else if (second) {
//            return "1";
//        } else if (third) {
//            return "2";
//        }
//
//        return "3";
//    }

    public static String[] parseHashMapOriginal (String hashmap) {
        String[] test;
        test = hashmap.split("\\{");
        hashmap = test[1];

        test = hashmap.split("}");
        hashmap = test[0];

        test = hashmap.split(", ");

        return test;
    }

    public static String[] parseLocalLevel (String local) {
        String[] test;
        if(local.contains(",")) {
            test = local.split(",");
        } else {
            String[] test1 = new String [0];
            test1[0] = local;
            return test1;
        }
        return test;
    }

    public static boolean isParentVertex (String vertex) {
        if (vertex.contains(".")) {
            String[] test;
            test = vertex.split(".");
            if (test[test.length].equals("0")) {
                return true;
            }
        } else {
            if (vertex.equals("0")) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasEmptyField (String[] vertices) {
        if (vertices.length < 3) {
            return true;
        }

        return false;
    }

    public static String getEmptyField (String[] vertices) {
        String[] test;
        boolean first = true;
        boolean second = true;
        boolean third = true;

        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i].contains(".")) {
                test = vertices[i].split(".");
                if (test[test.length].equals("0")) {
                    first = false;
                } else if (test[test.length].equals("1")) {
                    second = false;
                } else if (test[test.length].equals("2")) {
                    third = false;
                }
            } else {
                if (vertices[i].equals("0")) {
                    first = false;
                } else if (vertices[i].equals("1")) {
                    second = false;
                } else if (vertices[i].equals("2")) {
                    third = false;
                }
            }
        }

        if (first) {
            return "0";
        } else if (second) {
            return "1";
        } else if (third) {
            return "2";
        }

        return "3";
    }

    public static boolean hasParent (String vertex) {
        String[] test;
        if (vertex.contains(".")) {
            test = vertex.split(".");
            if (test[test.length - 1].equals("1")) {
                return true;
            }
        } else {
            if (vertex.equals("1")) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasParentChild () {

        return false;
    }


    // New methods

    // Parse HashMap , remove brackets
    // {0=1,127.0.0.1,8126,--1=0,127.0.0.1,8127}
    // 0=1,127.0.0.1,8126,--1=0,127.0.0.1,8127
    // 0=1,127.0.0.1,8126 = test[0]
    // 1=0,127.0.0.1,8127 -- 2 = test[1]
    public static String[] parseHashMap (String hashmap) {
        String[] test;
        test = hashmap.split("\\{");
        hashmap = test[1];

        test = hashmap.split("}");
        hashmap = test[0];

        test = hashmap.split(",--");

        for (int i = 0; i < test.length; i++) {
            test[i] = test[i].replace("--", " ");
        }

        return test;
    }

    public static HashMap createHashMap (String[] hashMapList) {
        String[] test;
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (int i = 0;i < hashMapList.length; i++) {
            test = hashMapList[i].split("=");
            hashMap.put(test[0], test[1]);
        }

        return hashMap;
    }

    public static void extendHashMap (HashMap map) {
        String temp;
        HashMap<String, String> cloneMap = new HashMap<String, String>(map);
        Iterator it = cloneMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            temp = pair.getKey().toString();
            Object obj = map.remove(pair.getKey());
            map.put("0." + temp, obj);
        }
    }

    public static String extendId (String id) {
        return "0." + id;
    }

    // Parse Vertices , remove brackets
    // [0.0.1, 0.0.2, 0.0.3] = > 0.0.1, 0.0.2, 0.0.3
    // 0.0.1
    // 0.0.2
    // 0.0.3
    public static String[] parseVertices (String vertices) {
        String[] test;
        test = vertices.split("\\[");
        vertices = test[1];

        test = vertices.split("]");
        vertices = test[0];

        test = vertices.split(", ");

        return test;
    }

    // 0.0.0 => 9
    // 0.0.0.0 => 9
    // 0.0.0.0.0.0.0.0.0 =>
    public static int numberOfChildrenGlobal(String id) {
        int count = 0;
        int start = 3;
        int n = 3;
        String[] parseValue = id.split("\\.");
        for (int i = parseValue.length - 1; i >= 0; i--) {
            if (parseValue[i].equals("0")) {
                count++;
                if (count == start + 1) {
                    start *= n;
                }
                continue;
            } else {
                break;
            }
        }

        return start - 1;
//        return (int) Math.pow(3, Math.floor(count / 2.0)) - 1;
    }

    public static String getEmptyChildId(HashMap<String, String> map) {
        String[] id;
        boolean second = true;
        boolean third = true;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if(pair.getKey().toString().contains(".")) {
                id = pair.getKey().toString().split("\\.");
                if (id[id.length - 1].equals("1")) {
                    second = false;
                } else if (id[id.length - 1].equals("2")) {
                    third = false;
                }
            } else {
                if (pair.getKey().toString().equals("1")) {
                    second = false;
                } else if (pair.getKey().toString().equals("2")) {
                    third = false;
                }

            }
        }

        if (second) {
            return "1";
        } else if (third) {
            return "2";
        }

        return "0";
    }

    public static boolean isGlobalParent(String id) {
        if (!id.contains(".")) {
            return id.equals("0");
        }

        String[] parseValue = id.split("\\.");
        for (int i = 0; i < parseValue.length; i++) {
            if (!parseValue[i].equals("0")) {
                return false;
            }
        }

        return true;
    }

    public static boolean isLocalParent(String id) {
        if (!id.contains(".")) {
            return id.equals("0");
        }

        String[] parseValue = id.split("\\.");

        return parseValue[parseValue.length - 1].equals("0");
    }

    public static boolean isNode1(String id) {
        if (!id.contains(".")) {
            return id.equals("1");
        }

        String[] parseValue = id.split("\\.");

        return parseValue[parseValue.length - 1].equals("1");
    }

    public static boolean isNode2(String id) {
        if (!id.contains(".")) {
            return id.equals("2");
        }

        String[] parseValue = id.split("\\.");

        return parseValue[parseValue.length - 1].equals("2");
    }

    public static String[] hasFreeParent(HashMap map) {
        String[] test = null;
        String[] testReturn = new String[3];
        testReturn[0] = null;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            test = pair.getValue().toString().split(" ");
            if (test.length > 1) {
                if (Integer.parseInt(test[1]) > 0) {
                    testReturn[0] = pair.getKey().toString();
                    testReturn[1] = test[0];
                    testReturn[2] = test[1];
                    return testReturn;
                }
            }

        }

        return testReturn;
    }

    // 0.0   1.0   1.1
    public static String getParent (HashMap map) {
        int k = 0;
        boolean bestSet = true;
        int best = 0;
        int bestStatic = 0;
        String[] parseKey;
        String bestId = "";

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            parseKey = pair.getKey().toString().split("\\.");

            if (bestSet) {
                best = parseKey.length;
                bestStatic = parseKey.length;
                bestSet = false;
            }

            parseKey = pair.getKey().toString().split("\\.");
            if (parseKey[parseKey.length - 1].equals("0")) {
                k = 0;
                for (int i = parseKey.length - 1; i >= 0; i--) {
                    if (parseKey[i].equals("0")) {
                        k++;
                    } else {
                        if (best > k) {
                            best = k;
                            bestId = pair.getKey().toString();
                        }
                        break;
                    }
                }

                if (bestStatic == k) {
                    return pair.getKey().toString();
                }
            }
        }
        return bestId;
    }

    public static String getNode1 (HashMap map) {

        String[] parseKey;

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            parseKey = pair.getKey().toString().split("\\.");
            if (parseKey[parseKey.length - 1].equals(Storage.NODE_1)) {
                return pair.getKey().toString();
            }
        }

        return null;
    }

    public static String getNode2 (HashMap map) {

        String[] parseKey;

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            parseKey = pair.getKey().toString().split("\\.");
            if (parseKey[parseKey.length - 1].equals(Storage.NODE_2)) {
                return pair.getKey().toString();
            }
        }

        return null;
    }
}
