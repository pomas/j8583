package com.solab.iso8583;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestLllll {
  private MessageFactory<IsoMessage> mfact = new MessageFactory<>();

  @Before
  public void setup() throws IOException {
    mfact.setConfigPath("testLLLLL.xml");
    mfact.setAssignDate(false);
  }


  @Test
  public void testTemplate() {
    IsoMessage m = mfact.newMessage(0x100);
    Assert.assertEquals("0100600000000000000000001X00002FF", m.debugString());
    m.setBinary(true);
    Assert.assertArrayEquals(new byte[]{1, 0, (byte) 0x60, 0, 0, 0, 0, 0, 0, 0,
        0, 1, (byte) 'X', 0, 1, (byte)0xff}, m.writeData());
  }

  @Test
  public void testNewMessage() {
    IsoMessage m = mfact.newMessage(0x200);
    m.setValue(2, "Variable length text", IsoType.LLLLLVAR, 0);
    m.setValue(3, "FFFF", IsoType.LLLLLBIN, 0);
    Assert.assertEquals("0200600000000000000000020Variable length text00004FFFF", m.debugString());
    m.setBinary(true);
    m.setValue(2, "XX", IsoType.LLLLLVAR, 0);
    m.setValue(3, new byte[]{(byte) 0xff, (byte) 0xff}, IsoType.LLLLLBIN, 0);

    Assert.assertArrayEquals(new byte[]{2, 0, (byte) 0x60, 0, 0, 0, 0, 0, 0, 0, 0, 2, (byte)'X', (byte)'X', 0, 2, (byte)0xff, (byte)0xff}, m.writeData());
  }

  @Test
  public void testParsing() throws ParseException, IOException {
    IsoMessage m = mfact.parseMessage("0100600000000000000000001X00002FF".getBytes(), 0);
    Assert.assertNotNull(m);
    Assert.assertEquals("X", m.getObjectValue(2));
    Assert.assertArrayEquals(new byte[]{(byte) 0xff}, (byte[])m.getObjectValue(3));
    mfact.setUseBinaryMessages(true);
    m = mfact.parseMessage(new byte[]{1, 0, (byte) 0x60, 0, 0, 0, 0, 0, 0, 0,
        0, 2, (byte)'X', (byte)'X', 0, 0, 10, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff ,  (byte)0xff, (byte)0xff}, 0);
    Assert.assertNotNull(m);
    Assert.assertEquals("XX", m.getObjectValue(2));
    Assert.assertArrayEquals(new byte[]{(byte) 0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff ,  (byte)0xff, (byte)0xff}, (byte[])m.getObjectValue(3));
  }
}
