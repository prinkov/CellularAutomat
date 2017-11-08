package xyz.prinkov.mmlab3;

import java.util.Random;

public class Table {
    public int[][] table;
    int[] coef;

    public Table(int n, int m) {
        Random rnd = new Random();
        table = new int[n][m];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++) {
                table[i][j] = 0;
        }
    }

    public void rnd() {
        Random rnd = new Random();

        for(int i = 0; i < table.length; i++)
            for(int j = 0; j < table[0].length; j++) {
                table[i][j] = rnd.nextInt() % 2;
            }
    }

    public int getValue(int[] vars) {
        int val = 0;
        for(int i = 0; i < coef.length; i++) {
            if (coef[i] == 0)
                continue;
            int curVal = 1;
            curVal *= coef[i];
            String s = String.format("%"+vars.length+"s",
                    Integer.toBinaryString(i)).replace(' ', '0');
            for(int j = 0; j < s.length(); j++)
                if(s.charAt(j) == '1')
                    curVal *= vars[j];
            val += curVal;
        }
        return val % 2;
    }

    public void computeIteration() {
        int[][] nextTable = new int[table.length][table[0].length];
        int n = (int)(Math.log(coef.length) / Math.log(2));
            for(int j = 0; j < table[0].length; j++)
                for(int i = 0; i < table.length; i++) {
                    int left = 0;
                    int top = 0;
                    int right = 0;
                    int bottom = 0;

                    if(i > 0 ) {
                        left = table[i - 1][j];
                    }
                    if(i < table.length - 1)
                        right = table[i + 1][j];

                    if(j > 0)
                        bottom = table[i][j - 1];

                    if(j < table[0].length - 1)
                        top = table[i][j + 1];

//                    System.out.println(i +  ":" +j + ": "+ "x_1=" + table[i][j] + ", x_2="+left +
//                        ", x_3=" + right + ", x_4=" +bottom + ", x_5 = " + top);
                    nextTable[i][j] = getValue(new int[]{table[i][j], left,
                                right, bottom, top});
            }
        table = nextTable;
    }

    public String computePolynom(int[] f) {
        int countVars = (int) (Math.log(f.length) / Math.log(2));
        coef = new int[f.length];
        boolean[][] set = new boolean[(int) Math.pow(2, countVars)]
                [countVars];
        for (int i = 0; i < set.length; i++) {
            String s = String.format("%"+countVars+"s",
                    Integer.toBinaryString(i)).replace(' ', '0');

            for (int j = set[0].length - s.length(); j < set[0].length; j++) {
                set[i][j] = s.charAt(j) == '1';
            }
            coef[i] = (f[i] + bSum(coef, i, set[i])) % 2;
        }
        String zhegalkinPolynom = "f = ";
        if(coef[0] > 0)
            zhegalkinPolynom = "f = " + coef[0] + " + ";
        for(int i = 1; i < coef.length; i++)
            if(coef[i] > 0) {
                String str = String.format("%"+countVars+"s",
                        Integer.toBinaryString(i)).replace(' ', '0');
                for(int j = 0; j < str.length(); j++)
                    if(str.charAt(j) == '1')
                        zhegalkinPolynom += "x_" + (j+1);
                if(i != coef.length - 1)
                    zhegalkinPolynom += " \\oplus ";
            }

            return zhegalkinPolynom;
    }

    private int bSum(int[] c, int index, boolean[] set) {
        int sum = c[0];

        for(int i = 1; i <= index; i++) {
            String s = String.format("%"+set.length+"s",
                    Integer.toBinaryString(i)).replace(' ', '0');
            int count = 0;
            for(int z = 0; z < s.length(); z++)
                if(s.charAt(z) == '1')
                    count++;
            boolean[] newSet = new boolean[count];
            count = 0;

            for(int z = 0; z < set.length; z++)
                if(s.charAt(z) == '1')
                    newSet[count++] = set[z];
            sum += c[i] * conjunction(newSet);
        }
        return sum % 2;
    }

    private int conjunction(boolean[] b) {
        for(int i = 0; i < b.length; i++)
            if(!b[i])
                return 0;
            return 1;
    }
}