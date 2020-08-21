// GetUserInput.java ユーザーからの入力を受け取る
package util;

import java.io.*;

/**
* @param: prompt -- 画面に表示する文字列
*                   例） 入力>
*
* @raturn: String -- ユーザーが入力した文字列
*/
public class GetUserInput {
    public static String get (String prompt) {
        String inputLine = null;
        System.out.print (prompt + " ");
        try {
            BufferedReader is = new BufferedReader(
                new InputStreamReader (System.in));
            inputLine = is.readLine();
            if (inputLine.length() == 0) return null;
        } catch (IOException e) {
            System.out.println ("IOException: " + e);
        }
        return inputLine;
    }
}

// 修正時刻: Sat Jul 18 17:43:47 2020
