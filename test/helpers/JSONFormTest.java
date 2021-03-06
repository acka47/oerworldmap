package helpers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


/**
 * @author fo
 */
public class JSONFormTest {

  @Test
  public void testFlatObject() {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo", new String[]{"bar"});
    formData.put("baz", new String[]{"bam"});
    String expected = "{\"foo\":\"bar\",\"baz\":\"bam\"}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testNestedObject() throws IOException {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[bar][baz]", new String[]{"bam"});
    formData.put("foo[bar][qux]", new String[]{"quux"});
    formData.put("foo[corge][grault]", new String[]{"fred"});
    formData.put("foo[garply]", new String[]{"waldo"});
    String expected = "{\"foo\":{\"bar\":{\"qux\":\"quux\",\"baz\":\"bam\"},\"corge\":{\"grault\":\"fred\"},\"garply\":\"waldo\"}}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFlatArrayWithNumericIndex() throws IOException {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[0]", new String[]{"bar"});
    formData.put("foo[1]", new String[]{"baz"});
    formData.put("foo[2]", new String[]{"bam"});
    String expected = "{\"foo\":[\"bar\",\"baz\",\"bam\"]}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFlatArrayWithImplicitIndex() {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo", new String[]{"bar", "baz", "bam"});
    String expected = "{\"foo\":[\"bar\",\"baz\",\"bam\"]}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testFlatArrayWithoutImplicitIndex() {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[]", new String[]{"bar", "baz", "bam"});
    String expected = "{\"foo\":[\"bar\",\"baz\",\"bam\"]}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testImplicitArrayWithSingleMember() throws IOException {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[]", new String[]{"bar"});
    String expected = "{\"foo\":[\"bar\"]}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testNestedArrayWithNumericIndex() throws IOException {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[0][0]", new String[]{"bar"});
    formData.put("foo[0][1]", new String[]{"baz"});
    formData.put("foo[0][2]", new String[]{"bam"});
    String expected = "{\"foo\":[[\"bar\",\"baz\",\"bam\"]]}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testSkippedIndex() throws IOException {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[0]", new String[]{"bar"});
    formData.put("foo[2]", new String[]{"bam"});
    String expected = "{\"foo\":[\"bar\",null,\"bam\"]}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testNestedArrayWithImplicitIndex() throws IOException {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[0]", new String[]{"bar", "baz", "bam"});
    String expected = "{\"foo\":[[\"bar\",\"baz\",\"bam\"]]}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testNestedArrayWithoutImplicitIndex() {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[0][]", new String[]{"bar", "baz", "bam"});
    String expected = "{\"foo\":[[\"bar\",\"baz\",\"bam\"]]}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

  @Test
  public void testInvalidPath() throws IOException {
    Map<String,String[]> formData= new HashMap<>();
    formData.put("foo[][bar]", new String[]{"bar", "baz", "bam"});
    formData.put("foo[bar][baz", new String[]{"bar"});
    formData.put("foo[0", new String[]{"bar"});
    formData.put("foo[", new String[]{"bar"});
    String expected = "{\"foo[bar][baz\":\"bar\",\"foo[\":\"bar\",\"foo[][bar]\":[\"bar\",\"baz\",\"bam\"],\"foo[0\":\"bar\"}";
    String result = JSONForm.parseFormData(formData).toString();
    assertEquals(expected, result);
  }

}
