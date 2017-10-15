package net.zive.shibayu.neo.dao.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
 * �f�[�^�A�N�Z�X�I�u�W�F�N�g���ۃN���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public abstract class AbstractFileAccessObject
            extends AbstractDataAccessObject implements FileAccessObject {
    /**
     * �w�b�_��`���X�g(�L�[�F����ID�A�l�F�����l).
     */
    private List<Map<String, String>> headerColumnAtters = null;

    /**
     * �{�f�B��`���X�g(�L�[�F����ID�A�l�F�����l).
     */
    private List<Map<String, String>> bodyColumnAtters = null;

    /**
     * �t�b�^�[��`���X�g(�L�[�F����ID�A�l�F�����l).
     */
    private List<Map<String, String>> futterColumnAtters = null;

    /**
     * �t�@�C�������݂���ꍇ�̏������@(�f�t�H���g=CANCEL).
     */
    private WriteMode writeMode = WriteMode.CANCEL;

    /**
     * �t�@�C���o�͑O���O�o�b�N�A�b�v�쐬�L���i�f�t�H���g=false:�o�b�N�A�b�v���Ȃ��j.
     */
    private boolean backupMode = false;

    /**
     * ���R�[�h�`�F�b�N�G���[���̏������@(�f�t�H���g=CANCEL).
     */
    private RecordErrorMode recordErrorMode = RecordErrorMode.CANCEL;

    /**
     * �R���X�g���N�^.
     * <ol>
     * <li>AbstractDataAccessObject�̃R���X�g���N�^�����s����B
     * <li>DAO��`�t�@�C����header��`��ǂݍ��ށB
     * <li>DAO��`�t�@�C����body��`��ǂݍ��ށB
     * <li>DAO��`�t�@�C����futter��`��ǂݍ��ށB
     * </ol>
     * @param reader DAO��`�t�@�C���Ǎ��p��XMLReader
     * @param logger ���K�[
     * @throws NeoFrameworkException �V�X�e���G���[
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
            info("header:" + headerColumnAtters);
            info("body:" + bodyColumnAtters);
            info("futter:" + futterColumnAtters);
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0001", e);
        }
    }

    @Override
    public final RecordSet read() throws NeoFrameworkException {
        try {
            File file = getFile();
            BufferedReader br = new BufferedReader(new FileReader(file));

            // TODO �t�@�C�����݃`�F�b�N��v�ǉ�
            // TODO �����R�[�h�̎w���v�ǉ�
            // TODO split������v�ǉ�
            String str = null;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
            }

            br.close();
        } catch (Exception e) {
            throw new NeoSystemException(e);
        }
        return null;
    }

    /**
     * �f�[�^�o�͂��s��.
     * <ol>
     * <li>�����Ώۃt�@�C���̎��O�`�F�b�N���s���B
     * <ul>
     * <li>�w��p�X�̑Ó����`�F�b�N���s���B
     * <li>�t�@�C���̑��݃`�F�b�N���s���B<br>
     * �t�@�C�������݂���ꍇ�AwriteMode��CANCEL�̏ꍇ�̓G���[
     * </ul>
     * <li>�o�b�N�A�b�v�t�@�C�����쐬����B<br>
     * backupMode��true�̏ꍇ�A�o�b�N�A�b�v�t�@�C�����쐬����B<br>
     * �o�b�N�A�b�v�t�@�C���͏����Ώۃt�@�C���Ɠ���f�B���N�g���Ƀt�@�C�����u�����Ώۃt�@�C����.yyyyMMddhhmmss.zip�v��
     * �쐬����B<br>
     * <li>�t�@�C���쐬���s���B<br>
     * ���R�[�h�Z�b�g�̃��R�[�h(Row)���Ɉȉ��̏������s���B
     * <ol>
     * <li>���R�[�h(Row)�̓��̓f�[�^�`�F�b�N���s���B<br>
     * ���̓`�F�b�N�G���[�����������ꍇ�A
     * <ul>
     * <li>recordErrorMode.CANCEL�̏ꍇ�A�����𒆒f����B<br>
     * �G���[����������O�܂ł̃��R�[�h�̓t�@�C���ɏo�͂���B
     * <li>recordErrorMode.SKIP�̏ꍇ�A�Ώۃ��R�[�h�̏o�͂𒆒f���㑱�̃��R�[�h�o�͂��s���B
     * <li>recordErrorMode.ROLLBACK�̏ꍇ�A�t�@�C���o�͑O�̏�Ԃɖ߂��B(��1)<br>
     * �o�b�N�A�b�v�t�@�C���őΏۃt�@�C�����㏑������B�o�b�N�A�b�v�t�@�C���͍폜����B<br>
     * </ul>
     * <li>���R�[�h(Row)���t�@�C���ɏo�͂���B(��2)(��3)
     * </ol>
     * </ol>
     * ��1 recordErrorMode.ROLLBACK�́AbackupMode��true�̏ꍇ�̂ݗL���ł��B<br>
             * �@�@backupMode��false�̏ꍇ�́ARecordErrorMode.CANCEL�Ɠ��l�̓���ƂȂ�܂��B<br>
     * ��2 writeMode.OVERWRITE�̏ꍇ�A�㏑���Ńt�@�C���쐬���s���B<br>
     * ��3 writeMode.APPEND�̏ꍇ�A�ǋL�Ńt�@�C���쐬���s���B<br>
     *
     * @param rec ���R�[�h�Z�b�g
     * @throws NeoFrameworkException �G���[
     */
    @Override
    public final void write(final RecordSet rec)
            throws NeoFrameworkException {
        String lineFeed = getLineFeed();
        String encode = getEncode();

        validateFile();
        String backupPath = createBackup();
        PrintWriter writer = createPrintWriter(encode);

        NeoUserException ue = new NeoUserException("NEO-A0008");

        for (int i = 0; i < rec.size(); i++) {
            Row row = rec.get(i);
            boolean first = (i == 0);
            boolean last = (i == rec.size() - 1);
            if (hockBeforeWrite(row)) {
                boolean skip = false;
                try {
                    validateRowCheck(row, first, last);
                } catch (NeoUserException e) {
                    if (recordErrorMode.equals(RecordErrorMode.ROLLBACK)
                            && backupPath != null) {
                        NeoUserException ce = new NeoUserException(
                                "NEO-A0005", i, "���[���o�b�N");
                        ce.addChild(e);
                        ue.addChild(ce);
                        break;
                    } else if (recordErrorMode.equals(RecordErrorMode.SKIP)) {
                        NeoUserException ce = new NeoUserException(
                                "NEO-A0005", i, "�X�L�b�v");
                        ce.addChild(e);
                        ue.addChild(ce);
                        skip = true;
                    } else if (recordErrorMode.equals(RecordErrorMode.IGNORE)) {
                        NeoUserException ce = new NeoUserException(
                                "NEO-A0005", i, "�������s");
                        ce.addChild(e);
                        ue.addChild(ce);
                    } else {
                        NeoUserException ce = new NeoUserException(
                                "NEO-A0005", i, "���f");
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
     * ���R�[�h�`�F�b�N�G���[���̏������@��ݒ肷��.<br>
     * ROLLBACK��backupMode��true�̏ꍇ�̂ݗL���ł��B<br>
     * ROLLBACK���w�肵���ꍇ��backupMode��false�̏ꍇ��CANCEL�Ɠ��l�̓���ƂȂ�܂��B<br>
     * ROLLBACK�����������ꍇ�A�o�b�N�A�b�v�t�@�C���͍폜����܂��B<br>
     * @param mode ���R�[�h�`�F�b�N�G���[���̏������@
     */
    @Override
    public final void setRecordErrorMode(final RecordErrorMode mode) {
        recordErrorMode = mode;
    }

    /**
     * �w�b�_��`Getter.
     * @return �w�b�_��`
     */
    protected List<Map<String, String>> getHeaderColumnAtters() {
        return headerColumnAtters;
    }

    /**
     * ���ג�`Getter.
     * @return ���ג�`
     */
    protected List<Map<String, String>> getBodyColumnAtters() {
        return bodyColumnAtters;
    }

    /**
     * �t�b�^��`Getter.
     * @return �t�b�^��`
     */
    protected List<Map<String, String>> getFutterColumnAtters() {
        return futterColumnAtters;
    }

    /**
     * �t�@�C�������݃��R�[�h��������.
     * @param row �o�͌��f�[�^
     * @param first �擪�s����
     * @param last �ŏI�s����
     * @return �t�@�C�������ݕ�����
     * @throws NeoSystemException �G���[
     */
    protected abstract String createWriteRecord(Row row,
                boolean first, boolean last) throws NeoSystemException;

    /**
     * �����Ώۃt�@�C���̎��O�`�F�b�N���s��.
     * @throws NeoFrameworkException ���O�`�F�b�N�G���[
     */
    private void validateFile() throws NeoFrameworkException {
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
     * �t�@�C�������݂���ꍇ�̏������@�ɉ�����PrintWriter�𐶐�����.
     * @param encode �G���R�[�h
     * @return PrintWriter
     * @throws NeoFrameworkException �t�@�C�������݃G���[
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
     * �o�b�N�A�b�v�t�@�C�����쐬����.<br>
     * �t�@�C�������݂�backupMode��true:"�o�b�N�A�b�v����"�̏ꍇ�A�o�b�N�A�b�v���쐬����.
     * @return �o�b�N�A�b�v�t�@�C����
     * @throws NeoFrameworkException �o�b�N�A�b�v�t�@�C���쐬���s
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
     * ���[���o�b�N���s��.
     * @param backupPath �o�b�N�A�b�v�t�@�C���p�X
     * @throws NeoFrameworkException  ���[���o�b�N���s
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
     * ���R�[�h�̓��̓`�F�b�N.
     * @param row �`�F�b�N�Ώۃ��R�[�h
     * @param first �擪�s����
     * @param last �ŏI�s����
     * @throws NeoFrameworkException ���̓`�F�b�N�G���[
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
     * �P���ڃ`�F�b�N.
     * @param row ���R�[�h
     * @param map ����
     * @param e �G���[�i�[ Exception
     * @throws NeoFrameworkException �V�X�e���G���[
     */
    private void columnCheck(final Row row, final Map<String, String> map,
            final NeoUserException e) throws NeoFrameworkException {
        String id = map.get("id");
        if (!row.containsKey(id)) {
            e.addChild(new NeoUserException("NEO-A0007", id));
        } else {
            int length = Integer.parseInt(map.get("length"));
            String value = row.getString(id);
            try {
                if (StringUtil.len(value) > length) {
                    e.addChild(new NeoUserException("NEO-A0009", id));
                }
            } catch (UnsupportedEncodingException e1) {
                throw new NeoSystemException(e1);
            }
        }
    }

    /**
     * ���s�R�[�h��Ԃ�.
     * @return ���s�R�[�h
     */
    private String getLineFeed() {
        String ln = getCommonAtters().get("lineFeed");
        if ("CRLF".equals(ln)) {
            return "\r\n";
        }
        return "\n";
    }

    /**
     * �G���R�[�h��Ԃ�.
     * @return �G���R�[�h
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
