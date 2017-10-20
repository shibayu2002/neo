package net.zive.shibayu.neo.dao.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.zive.shibayu.neo.dao.FileAccessObject;
import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.lang.NeoUserException;
import net.zive.shibayu.neo.lang.RecordSet;
import net.zive.shibayu.neo.lang.Row;
import net.zive.shibayu.neo.util.StringUtil;
import net.zive.shibayu.neo.util.XMLReader;

/**
 * データアクセスオブジェクト抽象クラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public abstract class AbstractFileAccessObject
            extends AbstractDataAccessObject implements FileAccessObject {
    /**
     * ヘッダ定義リスト(キー：属性ID、値：属性値).
     */
    private List<Map<String, String>> headerColumnAtters = null;

    /**
     * ボディ定義リスト(キー：属性ID、値：属性値).
     */
    private List<Map<String, String>> bodyColumnAtters = null;

    /**
     * フッター定義リスト(キー：属性ID、値：属性値).
     */
    private List<Map<String, String>> futterColumnAtters = null;

    /**
     * ファイルが存在する場合の処理方法(デフォルト=CANCEL).
     */
    private WriteMode writeMode = WriteMode.CANCEL;

    /**
     * ファイル出力前事前バックアップ作成有無（デフォルト=false:バックアップしない）.
     */
    private boolean backupMode = false;

    /**
     * レコードチェックエラー時の処理方法(デフォルト=CANCEL).
     */
    private RecordErrorMode recordErrorMode = RecordErrorMode.CANCEL;

    /**
     * コンストラクタ.
     * <ol>
     * <li>AbstractDataAccessObjectのコンストラクタを実行する。
     * <li>DAO定義ファイルのheader定義を読み込む。
     * <li>DAO定義ファイルのbody定義を読み込む。
     * <li>DAO定義ファイルのfutter定義を読み込む。
     * </ol>
     * @param reader DAO定義ファイル読込用のXMLReader
     * @param logger ロガー
     * @throws NeoFrameworkException システムエラー
     */
    public AbstractFileAccessObject(final XMLReader reader,
            final Logger logger) throws NeoFrameworkException {
        super(reader, logger);

        try {
            headerColumnAtters = createNodeDefList(
                    reader.readNodeSet("//header/column"));
            bodyColumnAtters = createNodeDefList(
                    reader.readNodeSet("//body/column"));
            futterColumnAtters = createNodeDefList(
                    reader.readNodeSet("//futter/column"));
            config("header:" + headerColumnAtters);
            config("body:" + bodyColumnAtters);
            config("futter:" + futterColumnAtters);
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0001", e);
        }
    }

    /**
     * データ入力を行う.
     * <ol>
     * <li>処理対象ファイルの事前チェックを行う。
     * <ul>
     * <li>指定パスの妥当性チェックを行う。
     * <li>ファイルの存在チェックを行う。<br>
     * </ul>
     * <li>ファイル入力を行う。<br>
     * 行毎にファイルの読込を行いRecordSetにセットする。
     * <ol>
     * <li>行のデータチェックを行う。<br>
     * 入力チェックエラーが発生した場合、
     * <ul>
     * <li>recordErrorMode.CANCELの場合、処理を中断し例外をスローする。<br>
     * エラーが発生する前までの行はRecordSetにセットする。
     * <li>recordErrorMode.SKIPの場合、対象行の入力を中断し後続行の入力を行う。<br>
     * 全行の処理後に例外をスローする。
     * <li>recordErrorMode.IGNOREの場合、エラーを無視し対象レコードの入力を行う。<br>
     * 全行の処理後に例外をスローする。
     * <li>recordErrorMode.ROLLBACKの場合、例外をスローする。(*1)<br>
     * RecordSetはread()呼び出し前の状態とする。
     * </ul>
     * <li>RecordSetを返却する。
     * </ol>
     * </ol>
     * ※1 recordErrorMode.ROLLBACKは、backupModeがtrueの場合のみ有効です。<br>
             * 　　backupModeがfalseの場合は、RecordErrorMode.CANCELと同様の動作となります。<br>
     *  backupModeがfalseの方が性能が向上します。ROLLBACKを使用しない場合は、
     *  backupModeにfalseを設定することを推奨します。
     *
     * @param rec 読み込んだデータをセットするRecordSet
     * @throws NeoFrameworkException エラー
     */
    @Override
    public final void read(final RecordSet rec) throws NeoFrameworkException {
        String encode = getEncode();

        RecordSet newRec = new RecordSet();
        validateFileForRead();

        NeoUserException ue = new NeoUserException("NEO-A0011");
        try {
            File file = getFile();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), encode));

            String line = null;
            int i = 0;
            while ((line = br.readLine()) != null) {
                boolean first = (i == 0);
                boolean last = (i == newRec.size() - 1);

                Row row = null;
                boolean skip = false;
                try {
                    validateRowCheckForRead(line, first, last);
                    row = createRow(line, first, last);
                    validateRowCheck(row, first, last);
                } catch (NeoUserException e) {
                    if (recordErrorMode.equals(RecordErrorMode.ROLLBACK)) {
                        if (backupMode) {
                            NeoUserException ce = new NeoUserException(
                                    "NEO-A0005", i + 1, "ロールバック");
                            ce.addChild(e);
                            ue.addChild(ce);
                        } else {
                            NeoUserException ce = new NeoUserException(
                                    "NEO-A0005", i + 1, "中断");
                            ce.addChild(e);
                            ue.addChild(ce);
                        }
                        break;
                    } else if (recordErrorMode.equals(RecordErrorMode.SKIP)) {
                        NeoUserException ce = new NeoUserException(
                                "NEO-A0005", i + 1, "スキップ");
                        ce.addChild(e);
                        ue.addChild(ce);
                        skip = true;
                    } else if (recordErrorMode.equals(RecordErrorMode.IGNORE)) {
                        NeoUserException ce = new NeoUserException(
                                "NEO-A0005", i + 1, "強制実行");
                        ce.addChild(e);
                        ue.addChild(ce);
                    } else {
                        NeoUserException ce = new NeoUserException(
                                "NEO-A0005", i + 1, "中断");
                        ce.addChild(e);
                        ue.addChild(ce);
                        break;
                    }
                }
                if (!skip && row != null) {
                    if (backupMode) {
                        newRec.add(row);
                    } else {
                        rec.add(row);
                    }
                }
                i++;
            }

            br.close();
        } catch (Exception e) {
            throw new NeoSystemException(e);
        }

        if (ue.children().size() > 0) {
            if (!recordErrorMode.equals(RecordErrorMode.ROLLBACK)) {
                if (backupMode) {
                    for (Row row : newRec) {
                        rec.add(row);
                    }
                }
            }

            throw ue;
        } else {
            if (backupMode) {
                for (Row row : newRec) {
                    rec.add(row);
                }
            }
        }
    }

    /**
     * データ出力を行う.
     * <ol>
     * <li>処理対象ファイルの事前チェックを行う。
     * <ul>
     * <li>指定パスの妥当性チェックを行う。
     * <li>ファイルの存在チェックを行う。<br>
     * ファイルが存在する場合、writeModeがCANCELの場合はエラー
     * </ul>
     * <li>バックアップファイルを作成する。<br>
     * backupModeがtrueの場合、バックアップファイルを作成する。<br>
     * バックアップファイルは処理対象ファイルと同一ディレクトリにファイル名「処理対象ファイル名.yyyyMMddhhmmss.zip」で
     * 作成する。<br>
     * <li>ファイル作成を行う。<br>
     * レコードセットのレコード(Row)毎に以下の処理を行う。
     * <ol>
     * <li>レコード(Row)の入力データチェックを行う。<br>
     * 入力チェックエラーが発生した場合、
     * <ul>
     * <li>recordErrorMode.CANCELの場合、処理を中断する。<br>
     * エラーが発生する前までのレコードはファイルに出力する。
     * <li>recordErrorMode.SKIPの場合、対象レコードの出力を中断し後続のレコード出力を行う。
     * <li>recordErrorMode.IGNOREの場合、エラーを無視し対象レコードの出力を行う。
     * <li>recordErrorMode.ROLLBACKの場合、ファイル出力前の状態に戻す。(※1)<br>
     * バックアップファイルで対象ファイルを上書きする。バックアップファイルは削除する。<br>
     * </ul>
     * <li>レコード(Row)をファイルに出力する。(※2)(※3)
     * </ol>
     * </ol>
     * ※1 recordErrorMode.ROLLBACKは、backupModeがtrueの場合のみ有効です。<br>
             * 　　backupModeがfalseの場合は、RecordErrorMode.CANCELと同様の動作となります。<br>
     * ※2 writeMode.OVERWRITEの場合、上書きでファイル作成を行う。<br>
     * ※3 writeMode.APPENDの場合、追記でファイル作成を行う。<br>
     *
     * @param rec レコードセット
     * @throws NeoFrameworkException エラー
     */
    @Override
    public final void write(final RecordSet rec)
            throws NeoFrameworkException {
        String lineFeed = getLineFeed();
        String encode = getEncode();

        validateFileForWrite();
        String backupPath = createBackup();
        PrintWriter writer = createPrintWriter(encode);

        NeoUserException ue = new NeoUserException("NEO-A0008");

        for (int i = 0; i < rec.size(); i++) {
            Row row = rec.get(i);
            boolean first = (i == 0);
            boolean last = (i == rec.size() - 1);
            boolean skip = false;
            try {
                validateRowCheck(row, first, last);
            } catch (NeoUserException e) {
                if (recordErrorMode.equals(RecordErrorMode.ROLLBACK)
                        && backupPath != null) {
                    NeoUserException ce = new NeoUserException(
                            "NEO-A0005", i + 1, "ロールバック");
                    ce.addChild(e);
                    ue.addChild(ce);
                    break;
                } else if (recordErrorMode.equals(RecordErrorMode.SKIP)) {
                    NeoUserException ce = new NeoUserException(
                            "NEO-A0005", i + 1, "スキップ");
                    ce.addChild(e);
                    ue.addChild(ce);
                    skip = true;
                } else if (recordErrorMode.equals(RecordErrorMode.IGNORE)) {
                    NeoUserException ce = new NeoUserException(
                            "NEO-A0005", i + 1, "強制実行");
                    ce.addChild(e);
                    ue.addChild(ce);
                } else {
                    NeoUserException ce = new NeoUserException(
                            "NEO-A0005", i + 1, "中断");
                    ce.addChild(e);
                    ue.addChild(ce);
                    break;
                }
            }
            if (!skip) {
                writer.print(createWriteRecord(
                        row, first, last) + lineFeed);
            }
        }
        writer.close();

        if (ue.children().size() > 0) {
            if (recordErrorMode.equals(RecordErrorMode.ROLLBACK)
                    && backupPath != null) {
                rollback(backupPath);
                (new File(backupPath)).delete();
            }
            throw ue;
        }
    }

    @Override
    public final void changeFile(final File file) {
        getCommonAtters().put("dir", file.getParentFile().getPath());
        getCommonAtters().put("file", file.getName());
    }

    @Override
    public final File getFile() {
        String dir = getCommonAtters().get("dir").replace("\\", "/");
        if (dir.lastIndexOf("/") != dir.length() - 1) {
            dir = dir + "/";
        }

        return new File(dir + getCommonAtters().get("file"));
    }

    @Override
    public final void setWriteMode(final WriteMode mode) {
        writeMode = mode;
    }

    @Override
    public final void setBackupMode(final boolean mode) {
        backupMode = mode;
    }

    /**
     * レコードチェックエラー時の処理方法を設定する.<br>
     * ROLLBACKはbackupModeがtrueの場合のみ有効です。<br>
     * ROLLBACKを指定した場合にbackupModeがfalseの場合はCANCELと同様の動作となります。<br>
     * ROLLBACKが発生した場合、バックアップファイルは削除されます。<br>
     * @param mode レコードチェックエラー時の処理方法
     */
    @Override
    public final void setRecordErrorMode(final RecordErrorMode mode) {
        recordErrorMode = mode;
    }

    /**
     * ヘッダ定義Getter.
     * @return ヘッダ定義
     */
    protected List<Map<String, String>> getHeaderColumnAtters() {
        return headerColumnAtters;
    }

    /**
     * 明細定義Getter.
     * @return 明細定義
     */
    protected List<Map<String, String>> getBodyColumnAtters() {
        return bodyColumnAtters;
    }

    /**
     * フッタ定義Getter.
     * @return フッタ定義
     */
    protected List<Map<String, String>> getFutterColumnAtters() {
        return futterColumnAtters;
    }

    /**
     * ファイル書込みレコード生成処理.
     * @param row 出力元データ
     * @param first 先頭行判定
     * @param last 最終行判定
     * @return ファイル書込み文字列
     * @throws NeoSystemException エラー
     */
    protected abstract String createWriteRecord(Row row,
                boolean first, boolean last) throws NeoSystemException;

    /**
     * ファイル読込結果からRowを生成する.
     * @param line 入力元データ
     * @param first 先頭行判定
     * @param last 最終行判定
     * @return Row
     * @throws NeoSystemException エラー
     */
    protected abstract Row createRow(String line,
                boolean first, boolean last) throws NeoSystemException;

    /**
     * 処理対象ファイルの事前チェックを行う.
     * @throws NeoFrameworkException 事前チェックエラー
     */
    private void validateFileForWrite() throws NeoFrameworkException {
        File file = getFile();
        if (file.isDirectory()) {
            throw new NeoUserException("NEO-A0002", file.getPath());
        }
        if (!file.getParentFile().exists()) {
            throw new NeoUserException("NEO-A0003", file.getParentFile());
        }
        if (writeMode.equals(WriteMode.CANCEL)) {
            if (file.exists()) {
                throw new NeoUserException("NEO-A0004", file.getPath());
            }
        }
    }

    /**
     * 処理対象ファイルの事前チェックを行う.
     * @throws NeoFrameworkException 事前チェックエラー
     */
    private void validateFileForRead() throws NeoFrameworkException {
        File file = getFile();
        if (file.isDirectory()) {
            throw new NeoUserException("NEO-A0002", file.getPath());
        }
        if (!file.getParentFile().exists()) {
            throw new NeoUserException("NEO-A0003", file.getParentFile());
        }
        if (!file.exists()) {
            throw new NeoUserException("NEO-A0010", file.getPath());
        }

    }

    /**
     * ファイルが存在する場合の処理方法に応じたPrintWriterを生成する.
     * @param encode エンコード
     * @return PrintWriter
     * @throws NeoFrameworkException ファイル書込みエラー
     */
    private PrintWriter createPrintWriter(final String encode)
                throws NeoFrameworkException {
        try {
            if (writeMode.equals(WriteMode.APPEND)) {
                return new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(getFile(), true), encode)));
            } else {
                return new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(getFile(), false), encode)));
            }
        } catch (FileNotFoundException e) {
            throw new NeoUserException("NEO-A0001", e.getMessage());
        } catch (IOException e) {
            throw new NeoUserException("NEO-A0001", e.getMessage());
        }
    }

    /**
     * バックアップファイルを作成する.<br>
     * ファイルが存在しbackupModeがtrue:"バックアップする"の場合、バックアップを作成する.
     * @return バックアップファイル名
     * @throws NeoFrameworkException バックアップファイル作成失敗
     */
    private String createBackup() throws NeoFrameworkException {
        if (!backupMode) {
            return null;
        }

        File file = getFile();
        if (!file.exists()) {
            return null;
        }

        final int bufSize = 1024;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String buckupFilePath = file.getPath() + "."
                + sdf.format(System.currentTimeMillis()) + ".zip";
        ZipOutputStream zos = null;
        InputStream is = null;
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(
                    new FileOutputStream(new File(buckupFilePath))));
            byte[] buf = new byte[bufSize];
            ZipEntry entry = new ZipEntry(file.getName());
            zos.putNextEntry(entry);
            is = new BufferedInputStream(new FileInputStream(file));
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
        } catch (IOException e) {
            throw new NeoUserException("NEO-E0006", e.getMessage());
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                throw new NeoUserException("NEO-E0001", e.getMessage());
            }
        }
        return buckupFilePath;
    }

    /**
     * ロールバックを行う.
     * @param backupPath バックアップファイルパス
     * @throws NeoFrameworkException  ロールバック失敗
     */
    private void rollback(final String backupPath)
                throws NeoFrameworkException {
        FileInputStream fis = null;
        ZipInputStream archive = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(backupPath);
            archive = new ZipInputStream(fis);
            ZipEntry entry   = null;
            while ((entry = archive.getNextEntry()) != null) {
                File file = new File(getFile().getParentFile().getPath()
                        + "/" + entry.getName());
                if (entry.isDirectory()) {
                    file.mkdirs();
                    continue;
                }
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);

                int size = 0;
                final int bufSize = 1024;
                byte[] buf = new byte[bufSize];
                while ((size = archive.read(buf)) > 0) {
                    bos.write(buf, 0, size);
                }
                if (bos != null) {
                    bos.close();
                    bos = null;
                }
            }
            if (archive != null) {
                archive.close();
                archive = null;
            }
        } catch (IOException e) {
            throw new NeoUserException("NEO-E0007", e.getMessage());
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
                if (archive != null) {
                    archive.close();
                }
            } catch (IOException e) {
                throw new NeoUserException("NEO-E0001", e.getMessage());
            }
        }
    }

    /**
     * レコードの入力チェック.
     * @param row チェック対象レコード
     * @param first 先頭行判定
     * @param last 最終行判定
     * @throws NeoFrameworkException 入力チェックエラー
     */
    private void validateRowCheck(final Row row,
                final boolean first, final boolean last)
                throws NeoFrameworkException {
        NeoUserException e = new NeoUserException("NEO-A0006");
        if (getHeaderColumnAtters().size() > 0 && first) {
            for (Map<String, String> map : getHeaderColumnAtters()) {
                columnCheck(row, map, e);
            }
        } else if (getFutterColumnAtters().size() > 0 && last) {
            for (Map<String, String> map : getFutterColumnAtters()) {
                columnCheck(row, map, e);
            }
        } else {
            for (Map<String, String> map : getBodyColumnAtters()) {
                columnCheck(row, map, e);
            }
        }
        if (e.children().size() > 0) {
            throw e;
        }
    }

    /**
     * レコードの入力チェック.
     * @param line 行
     * @param first 先頭行判定
     * @param last 最終行判定
     * @throws NeoFrameworkException 入力チェックエラー
     */
    protected abstract void validateRowCheckForRead(
                String line, boolean first, boolean last)
                throws NeoFrameworkException;

    /**
     * 単項目チェック.
     * @param row レコード
     * @param map 属性
     * @param e エラー格納 Exception
     * @throws NeoFrameworkException システムエラー
     */
    private void columnCheck(final Row row,
            final Map<String, String> map,
            final NeoUserException e) throws NeoFrameworkException {
        String id = map.get("id");
        String required = map.get("required");

        if (!row.containsKey(id)) {
            e.addChild(new NeoUserException("NEO-A0007", id));
        } else {
            int length = Integer.parseInt(map.get("length"));
            String value = row.getString(id);
            if ("true".equals(required) && value.length() == 0) {
                e.addChild(new NeoUserException("NEO-A0012", id));
            }
            try {
                if (StringUtil.lenB(value) > length) {
                    e.addChild(new NeoUserException("NEO-A0009", id));
                }
            } catch (UnsupportedEncodingException e1) {
                throw new NeoSystemException(e1);
            }
        }
    }

    /**
     * 改行コードを返す.
     * @return 改行コード
     */
    private String getLineFeed() {
        String ln = getCommonAtters().get("lineFeed");
        if ("CRLF".equals(ln)) {
            return "\r\n";
        }
        return "\n";
    }

    /**
     * エンコードを返す.
     * @return エンコード
     */
    private String getEncode() {
        return getCommonAtters().get("encode");
    }

    @Override
    public final String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("commonAtters=")
            .append(getCommonAtters());
        buf.append(", headerColumnAtters=")
            .append(headerColumnAtters.toString());
        buf.append(", bodyColumnAtters=")
            .append(bodyColumnAtters.toString());
        buf.append(", futterColumnAtters=")
            .append(futterColumnAtters.toString());
        return buf.toString();
    }
}
