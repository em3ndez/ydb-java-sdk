package com.yandex.ydb.jdbc.settings;

import java.sql.DriverPropertyInfo;

import javax.annotation.Nullable;

public interface ToDriverPropertyInfo {

    default DriverPropertyInfo toDriverPropertyInfoFrom(ParsedProperty value) {
        return toDriverPropertyInfo(value != null ? value.getValue() : null);
    }

    DriverPropertyInfo toDriverPropertyInfo(@Nullable String value);
}
