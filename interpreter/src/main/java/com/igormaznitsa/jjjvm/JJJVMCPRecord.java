package com.igormaznitsa.jjjvm;

import com.igormaznitsa.jjjvm.JJJVMConstantPool;

public final class JJJVMCPRecord {
  /**
   * Constant pool UTF8 string item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.7}
   */
  public static final int CONSTANT_UTF8 = 1;
  /**
   * Constant pool UNICODE string item.
   */
  public static final int CONSTANT_UNICODE = 2;
  /**
   * Constant pool INTEGER item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.4}
   */
  public static final int CONSTANT_INTEGER = 3;
  /**
   * Constant pool FLOAT item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.4}
   */
  public static final int CONSTANT_FLOAT = 4;
  /**
   * Constant pool LONG item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.5}
   */
  public static final int CONSTANT_LONG = 5;
  /**
   * Constant pool DOUBLE item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.5}
   */
  public static final int CONSTANT_DOUBLE = 6;
  /**
   * Constant pool Class Reference item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.1}
   */
  public static final int CONSTANT_CLASSREF = 7;
  /**
   * Constant pool String Reference item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.3}
   */
  public static final int CONSTANT_STRING = 8;
  /**
   * Constant pool Field Reference item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.2}
   */
  public static final int CONSTANT_FIELDREF = 9;
  /**
   * Constant pool Method Reference item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.2}
   */
  public static final int CONSTANT_METHODREF = 10;
  /**
   * Constant pool INTERFACE METHOD instance.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.2}
   */
  public static final int CONSTANT_INTERFACEMETHOD = 11;
  /**
   * Constant pool NAME+TYPE Reference item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.6}
   */
  public static final int CONSTANT_NAMETYPEREF = 12;
  /**
   * Constant pool Method Handle item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.8}
   */
  public static final int CONSTANT_METHODHANDLE = 15;
  /**
   * Constant pool Method Type item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.9}
   */
  public static final int CONSTANT_METHODTYPE = 16;
  /**
   * Constant pool Invoke dynamic item.
   * {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.10}
   */
  public static final int CONSTANT_INVOKEDYNAMIC = 18;
  private final int type;
  private final Object value;
  private final JJJVMConstantPool cpool;

  public JJJVMCPRecord(final JJJVMConstantPool cp, final int type, final Object value) {
    this.cpool = cp;
    this.type = type;
    this.value = value;
  }

  public int getType() {
    return this.type;
  }

  public Object getValue() {
    return this.value;
  }

  public int asInt() {
    return (Integer) this.value;
  }

  public Integer asInteger() {
    return (Integer) this.value;
  }

  public Double asDouble() {
    return (Double) this.value;
  }

  public Float asFloat() {
    return (Float) this.value;
  }

  public Long asLong() {
    return (Long) this.value;
  }

  public String asString() {
    switch (this.type) {
      case CONSTANT_UTF8:
      case CONSTANT_UNICODE:
        return (String) this.value;
      case CONSTANT_CLASSREF:
      case CONSTANT_STRING:
        return (String) this.cpool.getItem(this.asInt()).asObject();
      default:
        throw new IllegalArgumentException("Can't be presented as String [" + this.type + ']');
    }
  }

  private int extractHighUShort() {
    return this.asInt() >>> 16;
  }

  private int extractLowUShort() {
    return this.asInt() & 0xFFFF;
  }

  public String getClassName() {
    final String result;
    switch (this.type) {
      case CONSTANT_CLASSREF:
        {
          result = this.cpool.getItem(this.asInt()).asString();
        }
        break;
      case CONSTANT_METHODREF:
      case CONSTANT_INTERFACEMETHOD:
      case CONSTANT_FIELDREF:
        {
          result = this.cpool.getItem(extractHighUShort()).asString();
        }
        break;
      default:
        {
          throw new IllegalArgumentException("Illegal constant pool item");
        }
    }
    return result;
  }

  public String getSignature() {
    final String result;
    switch (this.type) {
      case CONSTANT_NAMETYPEREF:
        {
          result = this.cpool.getItem(this.extractLowUShort()).asString();
        }
        break;
      case CONSTANT_METHODREF:
      case CONSTANT_INTERFACEMETHOD:
      case CONSTANT_FIELDREF:
        {
          result = this.cpool.getItem(this.cpool.getItem(extractLowUShort()).extractLowUShort()).asString();
        }
        break;
      default:
        {
          throw new IllegalArgumentException("Illegal constant pool item");
        }
    }
    return result;
  }

  public String getName() {
    final String result;
    switch (this.type) {
      case CONSTANT_NAMETYPEREF:
        {
          result = this.cpool.getItem(this.extractHighUShort()).asString();
        }
        break;
      case CONSTANT_METHODREF:
      case CONSTANT_INTERFACEMETHOD:
      case CONSTANT_FIELDREF:
        {
          result = this.cpool.getItem(this.cpool.getItem(this.extractLowUShort()).extractHighUShort()).asString();
        }
        break;
      default:
        {
          throw new IllegalArgumentException("Illegal constant pool item");
        }
    }
    return result;
  }

  public Object asObject() {
    return this.value;
  }
  
}
