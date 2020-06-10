package com.yandex.ydb.persqueue.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler",
    comments = "Source: kikimr/public/api/grpc/draft/ydb_persqueue_v1.proto")
public final class PersQueueServiceGrpc {

  private PersQueueServiceGrpc() {}

  public static final String SERVICE_NAME = "Ydb.PersQueue.V1.PersQueueService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage,
      com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage> getStreamingWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamingWrite",
      requestType = com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage.class,
      responseType = com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage,
      com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage> getStreamingWriteMethod() {
    io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage, com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage> getStreamingWriteMethod;
    if ((getStreamingWriteMethod = PersQueueServiceGrpc.getStreamingWriteMethod) == null) {
      synchronized (PersQueueServiceGrpc.class) {
        if ((getStreamingWriteMethod = PersQueueServiceGrpc.getStreamingWriteMethod) == null) {
          PersQueueServiceGrpc.getStreamingWriteMethod = getStreamingWriteMethod =
              io.grpc.MethodDescriptor.<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage, com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamingWrite"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage.getDefaultInstance()))
              .setSchemaDescriptor(new PersQueueServiceMethodDescriptorSupplier("StreamingWrite"))
              .build();
        }
      }
    }
    return getStreamingWriteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage,
      com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage> getStreamingReadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamingRead",
      requestType = com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage.class,
      responseType = com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage,
      com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage> getStreamingReadMethod() {
    io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage, com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage> getStreamingReadMethod;
    if ((getStreamingReadMethod = PersQueueServiceGrpc.getStreamingReadMethod) == null) {
      synchronized (PersQueueServiceGrpc.class) {
        if ((getStreamingReadMethod = PersQueueServiceGrpc.getStreamingReadMethod) == null) {
          PersQueueServiceGrpc.getStreamingReadMethod = getStreamingReadMethod =
              io.grpc.MethodDescriptor.<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage, com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamingRead"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage.getDefaultInstance()))
              .setSchemaDescriptor(new PersQueueServiceMethodDescriptorSupplier("StreamingRead"))
              .build();
        }
      }
    }
    return getStreamingReadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse> getGetReadSessionsInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetReadSessionsInfo",
      requestType = com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest.class,
      responseType = com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse> getGetReadSessionsInfoMethod() {
    io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse> getGetReadSessionsInfoMethod;
    if ((getGetReadSessionsInfoMethod = PersQueueServiceGrpc.getGetReadSessionsInfoMethod) == null) {
      synchronized (PersQueueServiceGrpc.class) {
        if ((getGetReadSessionsInfoMethod = PersQueueServiceGrpc.getGetReadSessionsInfoMethod) == null) {
          PersQueueServiceGrpc.getGetReadSessionsInfoMethod = getGetReadSessionsInfoMethod =
              io.grpc.MethodDescriptor.<com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetReadSessionsInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PersQueueServiceMethodDescriptorSupplier("GetReadSessionsInfo"))
              .build();
        }
      }
    }
    return getGetReadSessionsInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse> getDescribeTopicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DescribeTopic",
      requestType = com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest.class,
      responseType = com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse> getDescribeTopicMethod() {
    io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse> getDescribeTopicMethod;
    if ((getDescribeTopicMethod = PersQueueServiceGrpc.getDescribeTopicMethod) == null) {
      synchronized (PersQueueServiceGrpc.class) {
        if ((getDescribeTopicMethod = PersQueueServiceGrpc.getDescribeTopicMethod) == null) {
          PersQueueServiceGrpc.getDescribeTopicMethod = getDescribeTopicMethod =
              io.grpc.MethodDescriptor.<com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DescribeTopic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PersQueueServiceMethodDescriptorSupplier("DescribeTopic"))
              .build();
        }
      }
    }
    return getDescribeTopicMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse> getDropTopicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DropTopic",
      requestType = com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest.class,
      responseType = com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse> getDropTopicMethod() {
    io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse> getDropTopicMethod;
    if ((getDropTopicMethod = PersQueueServiceGrpc.getDropTopicMethod) == null) {
      synchronized (PersQueueServiceGrpc.class) {
        if ((getDropTopicMethod = PersQueueServiceGrpc.getDropTopicMethod) == null) {
          PersQueueServiceGrpc.getDropTopicMethod = getDropTopicMethod =
              io.grpc.MethodDescriptor.<com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DropTopic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PersQueueServiceMethodDescriptorSupplier("DropTopic"))
              .build();
        }
      }
    }
    return getDropTopicMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse> getCreateTopicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateTopic",
      requestType = com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest.class,
      responseType = com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse> getCreateTopicMethod() {
    io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse> getCreateTopicMethod;
    if ((getCreateTopicMethod = PersQueueServiceGrpc.getCreateTopicMethod) == null) {
      synchronized (PersQueueServiceGrpc.class) {
        if ((getCreateTopicMethod = PersQueueServiceGrpc.getCreateTopicMethod) == null) {
          PersQueueServiceGrpc.getCreateTopicMethod = getCreateTopicMethod =
              io.grpc.MethodDescriptor.<com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateTopic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PersQueueServiceMethodDescriptorSupplier("CreateTopic"))
              .build();
        }
      }
    }
    return getCreateTopicMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse> getAlterTopicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AlterTopic",
      requestType = com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest.class,
      responseType = com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest,
      com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse> getAlterTopicMethod() {
    io.grpc.MethodDescriptor<com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse> getAlterTopicMethod;
    if ((getAlterTopicMethod = PersQueueServiceGrpc.getAlterTopicMethod) == null) {
      synchronized (PersQueueServiceGrpc.class) {
        if ((getAlterTopicMethod = PersQueueServiceGrpc.getAlterTopicMethod) == null) {
          PersQueueServiceGrpc.getAlterTopicMethod = getAlterTopicMethod =
              io.grpc.MethodDescriptor.<com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest, com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AlterTopic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PersQueueServiceMethodDescriptorSupplier("AlterTopic"))
              .build();
        }
      }
    }
    return getAlterTopicMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PersQueueServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PersQueueServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PersQueueServiceStub>() {
        @java.lang.Override
        public PersQueueServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PersQueueServiceStub(channel, callOptions);
        }
      };
    return PersQueueServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PersQueueServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PersQueueServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PersQueueServiceBlockingStub>() {
        @java.lang.Override
        public PersQueueServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PersQueueServiceBlockingStub(channel, callOptions);
        }
      };
    return PersQueueServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PersQueueServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PersQueueServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PersQueueServiceFutureStub>() {
        @java.lang.Override
        public PersQueueServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PersQueueServiceFutureStub(channel, callOptions);
        }
      };
    return PersQueueServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class PersQueueServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage> streamingWrite(
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(getStreamingWriteMethod(), responseObserver);
    }

    /**
     * <pre>
     **
     * Creates Read Session
     * Pipeline:
     * client                  server
     *         Init(Topics, ClientId, ...)
     *        ----------------&gt;
     *         Init(SessionId)
     *        &lt;----------------
     *         read1
     *        ----------------&gt;
     *         read2
     *        ----------------&gt;
     *         assign(Topic1, Cluster, Partition1, ...) - assigns and releases are optional
     *        &lt;----------------
     *         assign(Topic2, Clutster, Partition2, ...)
     *        &lt;----------------
     *         start_read(Topic1, Partition1, ...) - client must respond to assign request with this message. Only after this client will start recieving messages from this partition
     *        ----------------&gt;
     *         release(Topic1, Partition1, ...)
     *        &lt;----------------
     *         released(Topic1, Partition1, ...) - only after released server will give this parittion to other session.
     *        ----------------&gt;
     *         start_read(Topic2, Partition2, ...) - client must respond to assign request with this message. Only after this client will start recieving messages from this partition
     *        ----------------&gt;
     *         read data(data, ...)
     *        &lt;----------------
     *         commit(cookie1)
     *        ----------------&gt;
     *         committed(cookie1)
     *        &lt;----------------
     *         issue(description, ...)
     *        &lt;----------------
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage> streamingRead(
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(getStreamingReadMethod(), responseObserver);
    }

    /**
     * <pre>
     * Get information about reading
     * </pre>
     */
    public void getReadSessionsInfo(com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetReadSessionsInfoMethod(), responseObserver);
    }

    /**
     * <pre>
     * Describe topic command.
     * </pre>
     */
    public void describeTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDescribeTopicMethod(), responseObserver);
    }

    /**
     * <pre>
     * Drop topic command.
     * </pre>
     */
    public void dropTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDropTopicMethod(), responseObserver);
    }

    /**
     * <pre>
     * Create topic command.
     * </pre>
     */
    public void createTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateTopicMethod(), responseObserver);
    }

    /**
     * <pre>
     * Alter topic command.
     * </pre>
     */
    public void alterTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAlterTopicMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getStreamingWriteMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage,
                com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage>(
                  this, METHODID_STREAMING_WRITE)))
          .addMethod(
            getStreamingReadMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage,
                com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage>(
                  this, METHODID_STREAMING_READ)))
          .addMethod(
            getGetReadSessionsInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest,
                com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse>(
                  this, METHODID_GET_READ_SESSIONS_INFO)))
          .addMethod(
            getDescribeTopicMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest,
                com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse>(
                  this, METHODID_DESCRIBE_TOPIC)))
          .addMethod(
            getDropTopicMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest,
                com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse>(
                  this, METHODID_DROP_TOPIC)))
          .addMethod(
            getCreateTopicMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest,
                com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse>(
                  this, METHODID_CREATE_TOPIC)))
          .addMethod(
            getAlterTopicMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest,
                com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse>(
                  this, METHODID_ALTER_TOPIC)))
          .build();
    }
  }

  /**
   */
  public static final class PersQueueServiceStub extends io.grpc.stub.AbstractAsyncStub<PersQueueServiceStub> {
    private PersQueueServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PersQueueServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PersQueueServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteClientMessage> streamingWrite(
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getStreamingWriteMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     **
     * Creates Read Session
     * Pipeline:
     * client                  server
     *         Init(Topics, ClientId, ...)
     *        ----------------&gt;
     *         Init(SessionId)
     *        &lt;----------------
     *         read1
     *        ----------------&gt;
     *         read2
     *        ----------------&gt;
     *         assign(Topic1, Cluster, Partition1, ...) - assigns and releases are optional
     *        &lt;----------------
     *         assign(Topic2, Clutster, Partition2, ...)
     *        &lt;----------------
     *         start_read(Topic1, Partition1, ...) - client must respond to assign request with this message. Only after this client will start recieving messages from this partition
     *        ----------------&gt;
     *         release(Topic1, Partition1, ...)
     *        &lt;----------------
     *         released(Topic1, Partition1, ...) - only after released server will give this parittion to other session.
     *        ----------------&gt;
     *         start_read(Topic2, Partition2, ...) - client must respond to assign request with this message. Only after this client will start recieving messages from this partition
     *        ----------------&gt;
     *         read data(data, ...)
     *        &lt;----------------
     *         commit(cookie1)
     *        ----------------&gt;
     *         committed(cookie1)
     *        &lt;----------------
     *         issue(description, ...)
     *        &lt;----------------
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadClientMessage> streamingRead(
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getStreamingReadMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * Get information about reading
     * </pre>
     */
    public void getReadSessionsInfo(com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetReadSessionsInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Describe topic command.
     * </pre>
     */
    public void describeTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDescribeTopicMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Drop topic command.
     * </pre>
     */
    public void dropTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDropTopicMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Create topic command.
     * </pre>
     */
    public void createTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateTopicMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Alter topic command.
     * </pre>
     */
    public void alterTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest request,
        io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAlterTopicMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PersQueueServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<PersQueueServiceBlockingStub> {
    private PersQueueServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PersQueueServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PersQueueServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Get information about reading
     * </pre>
     */
    public com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse getReadSessionsInfo(com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetReadSessionsInfoMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Describe topic command.
     * </pre>
     */
    public com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse describeTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest request) {
      return blockingUnaryCall(
          getChannel(), getDescribeTopicMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Drop topic command.
     * </pre>
     */
    public com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse dropTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest request) {
      return blockingUnaryCall(
          getChannel(), getDropTopicMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Create topic command.
     * </pre>
     */
    public com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse createTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateTopicMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Alter topic command.
     * </pre>
     */
    public com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse alterTopic(com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest request) {
      return blockingUnaryCall(
          getChannel(), getAlterTopicMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PersQueueServiceFutureStub extends io.grpc.stub.AbstractFutureStub<PersQueueServiceFutureStub> {
    private PersQueueServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PersQueueServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PersQueueServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Get information about reading
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse> getReadSessionsInfo(
        com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetReadSessionsInfoMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Describe topic command.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse> describeTopic(
        com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDescribeTopicMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Drop topic command.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse> dropTopic(
        com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDropTopicMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Create topic command.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse> createTopic(
        com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateTopicMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Alter topic command.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse> alterTopic(
        com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAlterTopicMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_READ_SESSIONS_INFO = 0;
  private static final int METHODID_DESCRIBE_TOPIC = 1;
  private static final int METHODID_DROP_TOPIC = 2;
  private static final int METHODID_CREATE_TOPIC = 3;
  private static final int METHODID_ALTER_TOPIC = 4;
  private static final int METHODID_STREAMING_WRITE = 5;
  private static final int METHODID_STREAMING_READ = 6;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PersQueueServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PersQueueServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_READ_SESSIONS_INFO:
          serviceImpl.getReadSessionsInfo((com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoRequest) request,
              (io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.ReadInfoResponse>) responseObserver);
          break;
        case METHODID_DESCRIBE_TOPIC:
          serviceImpl.describeTopic((com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicRequest) request,
              (io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.DescribeTopicResponse>) responseObserver);
          break;
        case METHODID_DROP_TOPIC:
          serviceImpl.dropTopic((com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicRequest) request,
              (io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.DropTopicResponse>) responseObserver);
          break;
        case METHODID_CREATE_TOPIC:
          serviceImpl.createTopic((com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicRequest) request,
              (io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.CreateTopicResponse>) responseObserver);
          break;
        case METHODID_ALTER_TOPIC:
          serviceImpl.alterTopic((com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicRequest) request,
              (io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.AlterTopicResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAMING_WRITE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamingWrite(
              (io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingWriteServerMessage>) responseObserver);
        case METHODID_STREAMING_READ:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamingRead(
              (io.grpc.stub.StreamObserver<com.yandex.ydb.persqueue.YdbPersqueueV1.StreamingReadServerMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PersQueueServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PersQueueServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.yandex.ydb.persqueue.v1.YdbPersqueueV1.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PersQueueService");
    }
  }

  private static final class PersQueueServiceFileDescriptorSupplier
      extends PersQueueServiceBaseDescriptorSupplier {
    PersQueueServiceFileDescriptorSupplier() {}
  }

  private static final class PersQueueServiceMethodDescriptorSupplier
      extends PersQueueServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PersQueueServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PersQueueServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PersQueueServiceFileDescriptorSupplier())
              .addMethod(getStreamingWriteMethod())
              .addMethod(getStreamingReadMethod())
              .addMethod(getGetReadSessionsInfoMethod())
              .addMethod(getDescribeTopicMethod())
              .addMethod(getDropTopicMethod())
              .addMethod(getCreateTopicMethod())
              .addMethod(getAlterTopicMethod())
              .build();
        }
      }
    }
    return result;
  }
}
