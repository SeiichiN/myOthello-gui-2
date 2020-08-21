// GetConf.java
package util;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * settei.conf の内容を Map<String, String> の形で読み取る
 * 使い方:
 *   settei.conf
 *     username Seiichi
 *   Map<String, String> mapList = new GetConf("settei.conf").load();
 *   String username = mapList.get("username");
 *
 * ここでの使用法:
 *   settei.conf
 *     row 6
 *     col 6
 *     (空行)
 * 
 *   settei.conf の書式は以下のように決まっている。
 *     プロパティ名 (空白) 値 (改行)
 *     プロパティ名 (空白) 値 (改行)
 *     (改行のみの空行)
 *   BufferedReader の readLine は、改行を取り除いてくれる。
 *   読み取った行は line に入れている。
 *   そして addMap(line) で token[0] と token[1] という
 *   配列に入れている。
 *   settei.confの最終行が(改行)のみの空行でなかった場合、
 *   readLine は、NullPointerException を返す。
 *   それを避けるために(改行)のみの空行を入れている。
 */
public class GetConf {

    String filename = null;
    Map<String, String> confList = new HashMap<>();

    public GetConf(String fname) {
        this.filename = fname;
    }
    
    public Map<String, String> load () {
        try {
            File file = new File(filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = "";
            while (! (line = reader.readLine()).equals("")) {
                addMap(line);
            }
        } catch (FileNotFoundException fe) {
            System.out.println("ファイルがありません。");
            System.exit(1);
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return confList;
    }

    void addMap(String line) {
        String[] token = line.split(" ");
        // Arrays.stream( token ).forEach( ele -> System.out.println(ele) );
        if (token.length == 2) {
            confList.put(token[0], token[1]);   
        }
    }
}

// 修正時刻: Sat Jul 18 21:14:15 2020
