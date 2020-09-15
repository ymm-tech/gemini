package io.manbang.easybytecoder.plugin.replaytask.runtime.model;

public enum DataDiffTypeEnum {
    UNKNOW(-1, "unknow diff type"),
    FIELD_VALUE_DIFF(0, "field value is not same"),
    FIELD_MISSED(1, "field missed"),
    FIELD_OTHER_EXCEPTION(2, "other type field exception;like new field added, can not compare, etc"),
    FIELD_DISMACTH(3, "filed dismatch, contains all case");

    private int TypeCode;
    private String TypeName;

    DataDiffTypeEnum(int typeCode, String typeName) {
        TypeCode = typeCode;
        TypeName = typeName;
    }

    public int getTypeCode() {
        return TypeCode;
    }

    public String getTypeName() {
        return TypeName;
    }
}
