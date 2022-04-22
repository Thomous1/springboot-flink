package com.example.springflink.hbase;

import com.example.springflink.config.HbaseConfig;
import com.example.springflink.utils.ApplicationContextUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author wangzuoyu1
 * @description
 */
@Slf4j
public class HbaseClient {

    private static Admin admin;
    public static Connection conn;

    static {
        HbaseConfig hbaseConfig = ApplicationContextUtil.getBeanByType(HbaseConfig.class);
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.rootdir", hbaseConfig.getRootdir());
        conf.set("hbase.zookeeper.quorum", hbaseConfig.getZookeeper().getQuorum());
        conf.set("hbase.client.scanner.timeout.period", hbaseConfig.getClient().getPeriod() + "");
        conf.set("hbase.rpc.timeout", hbaseConfig.getRpc().getTimeout() + "");

        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName, String... columnFamilies) throws IOException {
        TableName tablename = TableName.valueOf(tableName);
        if (admin.tableExists(tablename)) {
            log.info("table {} exist");
        }else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tablename);
            for (String columnFamily: columnFamilies) {
                hTableDescriptor.addFamily(new HColumnDescriptor(columnFamily));
            }
            admin.createTable(hTableDescriptor);
        }
    }

    public static Result getDataResult(String tableName, String rowKey, String familyName, String column)
        throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        byte[] row = Bytes.toBytes(rowKey);
        Get get = new Get(row);
        Result result= table.get(get);
        return result;
    }

    /**
     * 获取一列获取一行数据
     * @param tableName
     * @param rowKey
     * @param famliyName
     * @param column
     * @return
     * @throws IOException
     */
    public static String getData(String tableName, String rowKey, String famliyName, String column) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        byte[] row = Bytes.toBytes(rowKey);
        Get get = new Get(row);
        Result result = table.get(get);
        byte[] resultValue = result.getValue(famliyName.getBytes(), column.getBytes());
        if (null == resultValue){
            return null;
        }
        return new String(resultValue);
    }

    /**
     * 获取一行的所有数据 并且排序
     * @param tableName 表名
     * @param rowKey 列名
     * @throws IOException
     */
    public static List<Entry> getRow(String tableName, String rowKey) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        byte[] row = Bytes.toBytes(rowKey);
        Get get = new Get(row);
        Result r = table.get(get);

        HashMap<String, Double> rst = new HashMap<>();

        for (Cell cell : r.listCells()){
            String key = Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
            String value = Bytes.toString(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength());
            rst.put(key, new Double(value));
        }

        List<Map.Entry> ans = new ArrayList<>();
        ans.addAll(rst.entrySet());

        Collections.sort(ans, (m1,m2) -> new Double((Double)m1.getValue()-(Double) m2.getValue()).intValue());

        return ans;
    }

    /**
     * 向对应列添加数据
     * @param tablename 表名
     * @param rowkey 行号
     * @param famliyname 列族名
     * @param column 列名
     * @param data 数据
     * @throws Exception
     */
    public static void putData(String tablename, String rowkey, String famliyname,String column,String data) throws Exception {
        Table table = conn.getTable(TableName.valueOf(tablename));
        Put put = new Put(rowkey.getBytes());
        put.addColumn(famliyname.getBytes(),column.getBytes(),data.getBytes());
        table.put(put);
    }

    /**
     * 将该单元格加1
     * @param tablename 表名
     * @param rowkey 行号
     * @param famliyname 列族名
     * @param column 列名
     * @throws Exception
     */
    public static void increamColumn(String tablename, String rowkey, String famliyname,String column) throws Exception {
        String val = getData(tablename, rowkey, famliyname, column);
        int res = 1;
        if (val != null) {
            res = Integer.valueOf(val) + 1;
        }
        putData(tablename, rowkey, famliyname, column, String.valueOf(res));
    }

    public static void main(String[] args) throws IOException {
        List<Map.Entry> ps = HbaseClient.getRow("stu", "1");
        ps.forEach(System.out::println);
    }


    /**
     * 取出表中所有的key
     * @param tableName
     * @return
     */
    public static List<String> getAllKey(String tableName) throws IOException {
        List<String> keys = new ArrayList<>();
        Scan scan = new Scan();
        Table table = HbaseClient.conn.getTable(TableName.valueOf(tableName));
        ResultScanner scanner = table.getScanner(scan);
        for (Result r : scanner) {
            keys.add(new String(r.getRow()));
        }
        return keys;
    }
}
