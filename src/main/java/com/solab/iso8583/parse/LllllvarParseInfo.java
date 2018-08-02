package com.solab.iso8583.parse;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import com.solab.iso8583.CustomField;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;

public class LllllvarParseInfo extends FieldParseInfo {

  public LllllvarParseInfo() {
    super(IsoType.LLLLLVAR, 0);
  }

  @Override
  public <T> IsoValue<?> parse(int field, byte[] buf, int pos, CustomField<T> custom) throws ParseException, UnsupportedEncodingException {
    if (pos < 0) {
      throw new ParseException(String.format(
          "Invalid LLLLLVAR field %d %d", field, pos), pos);
    } else if (pos+5 > buf.length) {
      throw new ParseException(String.format(
          "Insufficient data for LLLLLVAR header, pos %d", pos), pos);
    }
    final int len = decodeLength(buf, pos, 5);
    if (len < 0) {
      throw new ParseException(String.format(
          "Invalid LLLLLVAR length %d, field %d pos %d", len, field, pos), pos);
    } else if (len+pos+5 > buf.length) {
      throw new ParseException(String.format(
          "Insufficient data for LLLLLVAR field %d, pos %d", field, pos), pos);
    }
    String _v;
    try {
      _v = len == 0 ? "" : new String(buf, pos + 5, len, getCharacterEncoding());
    } catch (IndexOutOfBoundsException ex) {
      throw new ParseException(String.format(
          "Insufficient data for LLLLLVAR header, field %d pos %d", field, pos), pos);
    }
    //This is new: if the String's length is different from the specified
    // length in the buffer, there are probably some extended characters.
    // So we create a String from the rest of the buffer, and then cut it to
    // the specified length.
    if (_v.length() != len) {
      _v = new String(buf, pos + 5, buf.length-pos-5,
          getCharacterEncoding()).substring(0, len);
    }
    if (custom == null) {
      return new IsoValue<>(type, _v, len, null);
    } else {
      T dec = custom.decodeField(_v);
      return dec == null ? new IsoValue<>(type, _v, len, null) :
          new IsoValue<>(type, dec, len, custom);
    }
  }

  @Override
  public <T> IsoValue<?> parseBinary(int field, byte[] buf, int pos, CustomField<T> custom) throws ParseException, UnsupportedEncodingException {
    if (pos < 0) {
      throw new ParseException(String.format("Invalid bin LLLLLVAR field %d pos %d",
          field, pos), pos);
    } else if (pos+2 > buf.length) {
      throw new ParseException(String.format(
          "Insufficient data for bin LLLLLVAR header, field %d pos %d",
          field, pos), pos);
    }
    final int len = ((buf[pos - 1] & 0x0f) * 10000) + (((buf[pos] & 0xf0) >> 4) * 1000)
        + ((buf[pos] & 0x0f) * 100) + (((buf[pos + 1] & 0xf0) >> 4) * 10)
        + (buf[pos + 1] & 0x0f);
    if (len < 0) {
      throw new ParseException(String.format(
          "Invalid bin LLLLLVAR length %d, field %d pos %d", len, field, pos), pos);
    }
    if (len+pos+2 > buf.length) {
      throw new ParseException(String.format(
          "Insufficient data for bin LLLLLVAR field %d, pos %d", field, pos), pos);
    }
    if (custom == null) {
      return new IsoValue<>(type, new String(buf, pos + 2, len,
          getCharacterEncoding()), null);
    } else {
      T dec = custom.decodeField(new String(buf, pos + 2, len, getCharacterEncoding()));
      return dec == null ? new IsoValue<>(type,
          new String(buf, pos + 2, len, getCharacterEncoding()), null) :
          new IsoValue<>(type, dec, custom);
    }
  }
}
