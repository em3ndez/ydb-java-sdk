// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kikimr/public/api/grpc/draft/ydb_experimental_v1.proto

package com.yandex.ydb.experimental.v1;

public final class YdbExperimentalV1 {
  private YdbExperimentalV1() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n6kikimr/public/api/grpc/draft/ydb_exper" +
      "imental_v1.proto\022\023Ydb.Experimental.V1\032/k" +
      "ikimr/public/api/protos/ydb_experimental" +
      ".proto2\210\001\n\023ExperimentalService\022q\n\022Execut" +
      "eStreamQuery\022+.Ydb.Experimental.ExecuteS" +
      "treamQueryRequest\032,.Ydb.Experimental.Exe" +
      "cuteStreamQueryResponse0\001B \n\036com.yandex." +
      "ydb.experimental.v1b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.yandex.ydb.experimental.ExperimentalProtos.getDescriptor(),
        });
    com.yandex.ydb.experimental.ExperimentalProtos.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
