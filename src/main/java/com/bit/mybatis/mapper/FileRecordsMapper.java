package com.bit.mybatis.mapper;

import com.bit.mybatis.domain.FileRecords;

public interface FileRecordsMapper {

    FileRecords selectOne(String id);
}
