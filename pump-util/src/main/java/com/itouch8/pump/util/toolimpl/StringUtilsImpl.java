package com.itouch8.pump.util.toolimpl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

import com.itouch8.pump.core.util.CoreUtils;
import com.itouch8.pump.core.util.exception.Throw;
import com.itouch8.pump.util.Tool;

public abstract class StringUtilsImpl {

    private static final StringUtilsImpl instance = new StringUtilsImpl() {};

    private StringUtilsImpl() {}

    public static StringUtilsImpl getInstance() {
        return instance;
    }

    private static final AntPathMatcher antMatcher = new AntPathMatcher();

    public boolean isBlank(CharSequence cs) {
        return CoreUtils.isBlank(cs);
    }

    public String formatWhitespace(String src) {
        return CoreUtils.formatWhitespace(src);
    }

    public String formatString(String src, Object... objects) {
        return CoreUtils.format(src, objects);
    }

    public String convertToCamel(String str) {
        return CoreUtils.convertToCamel(str);
    }

    public boolean antMatch(String pattern, String path) {
        if (null == pattern || null == path) {
            return false;
        }
        return antMatcher.match(pattern, path);
    }

    public boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length()) {
            return false;
        }
        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public boolean endsWithIgnoreCase(String str, String suffix) {
        if (str == null || suffix == null) {
            return (str == null && suffix == null);
        }
        if (suffix.length() > str.length()) {
            return false;
        }
        int strOffset = str.length() - suffix.length();
        return str.regionMatches(true, strOffset, suffix, 0, suffix.length());
    }

    public boolean safeEquals(String str1, String str2) {
        return CoreUtils.safeEquals(str1, str2);
    }

    public boolean safeEqualsIgnoreCase(String str1, String str2) {
        return CoreUtils.safeEqualsIgnoreCase(str1, str2);
    }

    public String getMd5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public String getMd5(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            String md5 = DigestUtils.md5Hex(input);
            return md5;
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(input);
        }
    }

    public String getSha256(String str) {
        return DigestUtils.sha256Hex(str);
    }

    public String getSha256(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            String sha = DigestUtils.sha256Hex(input);
            return sha;
        } catch (Exception e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(input);
        }
    }

    public String encodeBase64(String data) {
        return Base64.encodeBase64String(data.getBytes());
    }

    public String encodeBase64(InputStream inputStream) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = inputStream.read(buffer))) {
            output.write(buffer, 0, n);
        }
        byte[] rsByte = output.toByteArray();
        return Base64.encodeBase64String(rsByte);
    }

    public String decodeBase64(String data) {
        byte[] s = Base64.decodeBase64(data);
        return new String(s);
    }

    public String encodeHex(String data) {
        return Hex.encodeHexString(data.getBytes());
    }

    public String decodeHex(String data) {
        try {
            byte[] s = Hex.decodeHex(data.toCharArray());
            return new String(s);
        } catch (DecoderException e) {
            Throw.throwRuntimeException(e);
            return null;
        }
    }

    public String escapeJava(String input) {
        return StringEscapeUtils.escapeJava(input);
    }

    public String unescapeJava(String input) {
        return StringEscapeUtils.unescapeJava(input);
    }

    public String escapeHtml4(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }

    public String unescapeHtml4(String input) {
        return StringEscapeUtils.unescapeHtml4(input);
    }

    public String escapeXml(String input) {
        return StringEscapeUtils.escapeXml10(input);
    }

    public String unescapeXml(String input) {
        return StringEscapeUtils.unescapeXml(input);
    }

    public String getRandomAlphanumeric(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

    public String getRandomNumeric(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    public String random(int count, char... chars) {
        return RandomStringUtils.random(count, chars);
    }

    public String getRandomNumericIncludeDate(int count) {
        return Tool.DATE.getDate() + RandomStringUtils.randomNumeric(count);
    }

    public String getRandomNumericIncludeTime(int count) {
        return Tool.DATE.getFormatDate(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomNumeric(count);
    }

    public String getRandomKeyId(int count) {
        return Tool.DATE.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(count);
    }

    public String getRandomKeyId() {
        return Tool.DATE.getFormatDate(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(15);
    }

    public List<String> splitToList(String src, String separator) {
        return CoreUtils.splitToList(src, separator);
    }

    public List<String> splitToList(String src, String separator, int minSize, String defaultString) {
        return CoreUtils.splitToList(src, separator, minSize, defaultString);
    }

    public String[] split(String src, String separator) {
        return CoreUtils.split(src, separator);
    }

    public String[] split(String src, String separator, int minLength, String defaultString) {
        return CoreUtils.split(src, separator, minLength, defaultString);
    }

    public String join(List<?> list, String separator) {
        return CoreUtils.join(list, separator);
    }

    public String join(Object[] arr, String separator) {
        return CoreUtils.join(arr, separator);
    }

    public String leftPad(final String str, final int size, final String padStr) {
        return StringUtils.leftPad(str, size, padStr);
    }

    public String rightPad(final String str, final int size, final String padStr) {
        return StringUtils.rightPad(str, size, padStr);
    }

    public String getListValue(List<String> list, int index, String defaultValue) {
        if (null == list || index < 0 || index >= list.size()) {
            return defaultValue;
        } else {
            return list.get(index);
        }
    }

    public String gzip(String str) {
        if (Tool.CHECK.isBlank(str)) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            return new String(out.toByteArray());
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(gzip);
        }
    }

    public String ungzip(String str) {
        if (Tool.CHECK.isBlank(str)) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        String decompressed = null;
        try {
            byte[] compressed = str.getBytes();
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);
            byte[] buffer = new byte[8096];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(ginzip, in, out);
        }
        return decompressed;
    }

    public String zip(String str) {
        if (Tool.CHECK.isBlank(str)) {
            return str;
        }
        ByteArrayOutputStream out = null;
        ZipOutputStream zout = null;
        String compressedStr = null;
        try {
            out = new ByteArrayOutputStream();
            zout = new ZipOutputStream(out);
            zout.putNextEntry(new ZipEntry("0"));
            zout.write(str.getBytes());
            zout.closeEntry();
            byte[] compressed = out.toByteArray();
            compressedStr = new String(compressed);
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(zout, out);
        }
        return compressedStr;
    }

    public String unzip(String str) {
        if (Tool.CHECK.isBlank(str)) {
            return str;
        }
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        ZipInputStream zin = null;
        String decompressed = null;
        try {
            byte[] compressed = str.getBytes();
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(compressed);
            zin = new ZipInputStream(in);
            zin.getNextEntry();
            byte[] buffer = new byte[8096];
            int offset = -1;
            while ((offset = zin.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } catch (IOException e) {
            throw Throw.createRuntimeException(e);
        } finally {
            Tool.IO.closeQuietly(zin, in, out);
        }
        return decompressed;
    }

    public void parseNamedValue(Properties properties, String text, String tpl) {
        if (null != text && null != tpl) {
            StringBuffer sb = new StringBuffer("\\Q");
            Matcher matcher = pattern.matcher(tpl);
            List<String> vars = new ArrayList<String>();
            while (matcher.find()) {
                vars.add(matcher.group(1));
                String length = matcher.group(2);
                String replace = "\\\\E(.";
                if (null != length) {
                    replace += "{" + length + "}";
                } else {
                    replace += "+";
                }
                replace += ")\\\\Q";
                matcher.appendReplacement(sb, replace);
            }
            matcher.appendTail(sb);
            sb.append("\\E");
            Pattern pp = Pattern.compile(sb.toString());
            matcher = pp.matcher(text);
            if (matcher.find()) {
                for (int i = 0, s = vars.size(); i < s; i++) {
                    String value = matcher.group(i + 1);
                    properties.put(vars.get(i), value == null ? "" : value);
                }
            }
        }
    }

    public String getRestUri(String uri) {
        if (null != uri) {
            int i = uri.lastIndexOf('.');
            if (-1 != i) {
                uri = uri.substring(0, i);
            }
        }
        return uri;
    }

    public boolean isValidColumnIndex(int columnIndex) {
        return columnIndex >= 0 && columnIndex < EXCEL_COLUMNS.size();
    }

    public boolean isValidColumnPosition(String columnPosition) {
        if (null == columnPosition) {
            return false;
        }
        return EXCEL_COLUMNS.contains(columnPosition.toUpperCase());
    }

    public int getColumnPositionIndex(String columnPosition) {
        if (null == columnPosition) {
            return -1;
        }
        return EXCEL_COLUMNS.indexOf(columnPosition.toUpperCase());
    }

    public String getColumnPosition(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= EXCEL_COLUMNS.size()) {
            throw new IllegalArgumentException("the index " + columnIndex + " is invalid.");
        }
        return EXCEL_COLUMNS.get(columnIndex);
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String getRandomStr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(RANDOM_STR.charAt(RANDOM.nextInt(RANDOM_STR.length())));
        }
        return sb.toString();
    }

    /**
     * 元转分
     * 
     * @param yuan
     * @return
     */
    public Integer Yuan2Fen(BigDecimal yuan) {
        return yuan.movePointRight(2).intValue();
    }

    /**
     * 分转元
     * 
     * @param fen
     * @return
     */
    public BigDecimal Fen2Yuan(Integer fen) {
        return new BigDecimal(fen).movePointLeft(2);
    }

    private static final String RANDOM_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final java.util.Random RANDOM = new java.util.Random();

    private static final Pattern pattern = Pattern.compile("\\$\\{\\s*(\\w+)\\s*(?:,\\s*(\\d+))?\\s*\\}");

    private static final List<String> EXCEL_COLUMNS = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN", "BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY", "BZ", "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ", "CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU", "CV", "CW", "CX", "CY", "CZ", "DA", "DB", "DC", "DD", "DE", "DF", "DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ", "DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ", "EA", "EB", "EC", "ED", "EE", "EF", "EG", "EH", "EI", "EJ", "EK", "EL", "EM", "EN", "EO", "EP", "EQ", "ER", "ES", "ET", "EU", "EV", "EW", "EX", "EY", "EZ", "FA", "FB", "FC",
            "FD", "FE", "FF", "FG", "FH", "FI", "FJ", "FK", "FL", "FM", "FN", "FO", "FP", "FQ", "FR", "FS", "FT", "FU", "FV", "FW", "FX", "FY", "FZ", "GA", "GB", "GC", "GD", "GE", "GF", "GG", "GH", "GI", "GJ", "GK", "GL", "GM", "GN", "GO", "GP", "GQ", "GR", "GS", "GT", "GU", "GV", "GW", "GX", "GY", "GZ", "HA", "HB", "HC", "HD", "HE", "HF", "HG", "HH", "HI", "HJ", "HK", "HL", "HM", "HN", "HO", "HP", "HQ", "HR", "HS", "HT", "HU", "HV", "HW", "HX", "HY", "HZ", "IA", "IB", "IC", "ID", "IE", "IF", "IG", "IH", "II", "IJ", "IK", "IL", "IM", "IN", "IO", "IP", "IQ", "IR", "IS", "IT", "IU", "IV");
}
