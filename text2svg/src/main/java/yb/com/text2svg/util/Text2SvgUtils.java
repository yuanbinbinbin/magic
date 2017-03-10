package yb.com.text2svg.util;


import android.util.Log;

import org.apache.batik.svggen.font.Font;
import org.apache.batik.svggen.font.Glyph;
import org.apache.batik.svggen.font.Point;
import org.apache.batik.svggen.font.table.CmapFormat;
import org.apache.batik.svggen.font.table.Table;
import org.apache.batik.util.SVGConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yb.com.text2svg.entity.CharGlyph;
import yb.com.text2svg.entity.FontFace;
import yb.com.text2svg.entity.ResultData;


/**
 * 文本转SVG PATH 工具
 * User: zhaodahao
 * Date: 16-11-3
 * <p/>
 * <p/>
 * Time: 下午2:37
 */
public class Text2SvgUtils implements SVGConstants {

    public static ResultData toConvert(String fontPath, String text) throws Exception {
        Font font = Font.create(fontPath);
        ResultData resultData = new ResultData();
        resultData.setCharGlyphList(getTextPath(font, text));
        return resultData;
    }

    protected static List<CharGlyph> getTextPath(Font font, String text) throws Exception {
        int horiz_advance_x = font.getOS2Table().getAvgCharWidth();
        CmapFormat cmapFmt = getCmapFormat(font);
        if (cmapFmt == null) {
            throw new Exception("Cannot find a suitable cmap table");
        }
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        // 获取文字对应的path
        char[] chArray = text.toCharArray();
        List<CharGlyph> charGlyphList = new ArrayList<CharGlyph>(chArray.length);
        for (char ch : chArray) {
            String code = Integer.toHexString(ch);
            int glyphIndex = cmapFmt.mapCharCode(Integer.parseInt(code, 16));
            CharGlyph charGlyph = getGlyphAsSVG(font, font.getGlyph(glyphIndex), glyphIndex, horiz_advance_x, null, XML_CHAR_REF_PREFIX + code + XML_CHAR_REF_SUFFIX);
            charGlyph.setSource(ch);
            charGlyphList.add(charGlyph);
        }
        return charGlyphList;
    }

    protected static CmapFormat getCmapFormat(Font font) {
        CmapFormat cmapFmt = font.getCmapTable().getCmapFormat(Table.platformMicrosoft, Table.encodingUGL);
        if (cmapFmt == null) {
            // This might be a symbol font, so we'll look for an "undefined" encoding
            cmapFmt = font.getCmapTable().getCmapFormat(Table.platformMicrosoft, Table.encodingUndefined);
        }
        return cmapFmt;
    }


    protected static FontFace getSVGFontFaceElement(Font font) {
        String fontFamily = font.getNameTable().getRecord(Table.nameFontFamilyName);
        short unitsPerEm = font.getHeadTable().getUnitsPerEm();
        String panose = font.getOS2Table().getPanose().toString();
        short ascent = font.getHheaTable().getAscender();
        short descent = font.getHheaTable().getDescender();

        FontFace fontFace = new FontFace();
        fontFace.setFontFamily(fontFamily);
        fontFace.setUnitsPerEm(unitsPerEm);
        fontFace.setAscent(ascent);
        fontFace.setDescent(descent);
        fontFace.setPanose(panose);

        return fontFace;
    }

    protected static CharGlyph getGlyphAsSVG(Font font, Glyph glyph, int glyphIndex, int defaultHorizAdvanceX, String attrib, String code) {
        CharGlyph charGlyph = new CharGlyph();
        int firstIndex = 0;
        int count = 0;
        int i;
        int horiz_advance_x;
        horiz_advance_x = font.getHmtxTable().getAdvanceWidth(glyphIndex);

        if (glyphIndex == 0) {
            // 不支持
            charGlyph.setMissing(true);
        } else {
            // Unicode value
            charGlyph.setUnicode(code);
        }
        if (horiz_advance_x != defaultHorizAdvanceX) {
            // sb.append(" horiz-adv-x=\"").append(horiz_advance_x).append("\"");
            charGlyph.setHorizAdvX(horiz_advance_x);
        } else {
            charGlyph.setHorizAdvX(defaultHorizAdvanceX);
        }
        if (glyph != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("<svg width=\"512\" height=\"512\" viewBox=\"0 0 512 512\">");
            for (i = 0; i < glyph.getPointCount(); i++) {
                count++;
                if (glyph.getPoint(i).endOfContour) {
                    sb.append("<path d=\"").append(getContourAsSVGPathData(glyph, firstIndex, count))
                            .append("\"></path>");
                    firstIndex = i + 1;
                    count = 0;
                }
                Log.e("test", "x:" + glyph.getPoint(i).x + "y:" + glyph.getPoint(i).y+"onCurve:"+glyph.getPoint(i).onCurve+"endOfContour:"+glyph.getPoint(i).endOfContour);
            }
            sb.append("</svg>");
            charGlyph.setD(sb.toString());
        }

        return charGlyph;
    }

    protected static String getContourAsSVGPathData(Glyph glyph, int startIndex, int count) {

        // If this is a single point on it's own, we can't do anything with it
        if (glyph.getPoint(startIndex).endOfContour) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int offset = 0;

        while (offset < count) {
            Point point = glyph.getPoint(startIndex + offset % count);
            Point point_plus1 = glyph.getPoint(startIndex + (offset + 1) % count);
            Point point_plus2 = glyph.getPoint(startIndex + (offset + 2) % count);

            if (offset == 0) {
                sb.append(PATH_MOVE)
                        .append(String.valueOf(point.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point.y))
                        .append(" ");
            }

            if (point.onCurve && point_plus1.onCurve) {
                if (point_plus1.x == point.x) { // This is a vertical line
                    sb.append(PATH_VERTICAL_LINE_TO)
                            .append(String.valueOf(point_plus1.y));
                } else if (point_plus1.y == point.y) { // This is a horizontal line
                    sb.append(PATH_HORIZONTAL_LINE_TO)
                            .append(String.valueOf(point_plus1.x));
                } else {
                    sb.append(PATH_LINE_TO)
                            .append(String.valueOf(point_plus1.x))
                            .append(XML_SPACE)
                            .append(String.valueOf(point_plus1.y));
                }
                offset++;
            } else if (point.onCurve && !point_plus1.onCurve && point_plus2.onCurve) {
                // This is a curve with no implied points
                sb.append(PATH_QUAD_TO)
                        .append(String.valueOf(point_plus1.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus1.y))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus2.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus2.y));
                offset += 2;
            } else if (point.onCurve && !point_plus1.onCurve && !point_plus2.onCurve) {
                // This is a curve with one implied point
                sb.append(PATH_QUAD_TO)
                        .append(String.valueOf(point_plus1.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus1.y))
                        .append(XML_SPACE)
                        .append(String.valueOf(midValue(point_plus1.x, point_plus2.x)))
                        .append(XML_SPACE)
                        .append(String.valueOf(midValue(point_plus1.y, point_plus2.y)));
                offset += 2;
            } else if (!point.onCurve && !point_plus1.onCurve) {
                // This is a curve with two implied points
                sb.append(PATH_SMOOTH_QUAD_TO)
                        .append(String.valueOf(midValue(point.x, point_plus1.x)))
                        .append(XML_SPACE)
                        .append(String.valueOf(midValue(point.y, point_plus1.y)));
                offset++;
            } else if (!point.onCurve && point_plus1.onCurve) {
                sb.append(PATH_SMOOTH_QUAD_TO)
                        .append(String.valueOf(point_plus1.x))
                        .append(XML_SPACE)
                        .append(String.valueOf(point_plus1.y));
                offset++;
            } else {
                System.out.println("drawGlyph case not catered for!!");
                break;
            }
        }
        sb.append(PATH_CLOSE);

        return sb.toString();
    }

    private static int midValue(int a, int b) {
        return a + (b - a) / 2;
    }


}
