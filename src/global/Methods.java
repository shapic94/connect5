package global;

public class Methods {

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

    // Parse HashMap , remove brackets
    // {0=1,127.0.0.1,8126,--1=0,127.0.0.1,8127}
    // 0=1,127.0.0.1,8126,--1=0,127.0.0.1,8127
    // 0=1,127.0.0.1,8126 = test[0]
    // 1=0,127.0.0.1,8127 = test[1]
    public static String[] parseHashMap (String hashmap) {
        String[] test;
        test = hashmap.split("\\{");
        hashmap = test[1];

        test = hashmap.split("}");
        hashmap = test[0];

        test = hashmap.split(",--");

        return test;
    }

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

    public static String getParent () {

        return "Test";
    }
}
