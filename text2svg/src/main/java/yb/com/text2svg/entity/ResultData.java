package yb.com.text2svg.entity;

import java.util.List;

/**
 * User: zhaodahao Date: 16-11-3 Time: 下午9:02
 */
public class ResultData {
    private List<CharGlyph> charGlyphList;

    public List<CharGlyph> getCharGlyphList() {
        return charGlyphList;
    }

    public void setCharGlyphList(List<CharGlyph> charGlyphList) {
        this.charGlyphList = charGlyphList;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "charGlyphList=" + charGlyphList +
                '}';
    }
}
