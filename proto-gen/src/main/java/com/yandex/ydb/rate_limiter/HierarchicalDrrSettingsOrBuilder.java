// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kikimr/public/api/protos/ydb_rate_limiter.proto

package com.yandex.ydb.rate_limiter;

public interface HierarchicalDrrSettingsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Ydb.RateLimiter.HierarchicalDrrSettings)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Resource consumption speed limit.
   * Value is required for root resource.
   * 0 is equivalent to not set.
   * Must be nonnegative.
   * </pre>
   *
   * <code>double max_units_per_second = 1;</code>
   */
  double getMaxUnitsPerSecond();

  /**
   * <pre>
   * Maximum burst size of resource consumption across the whole cluster
   * divided by max_units_per_second.
   * Default value is 1.
   * This means that maximum burst size might be equal to max_units_per_second.
   * 0 is equivalent to not set.
   * Must be nonnegative.
   * </pre>
   *
   * <code>double max_burst_size_coefficient = 2;</code>
   */
  double getMaxBurstSizeCoefficient();

  /**
   * <pre>
   * Prefetch in local bucket up to prefetch_coefficient*max_units_per_second units (full size).
   * Default value is inherited from parent or 0.2 for root.
   * Disables prefetching if any negative value is set
   * (It is useful to avoid bursts in case of large number of local buckets).
   * </pre>
   *
   * <code>double prefetch_coefficient = 3;</code>
   */
  double getPrefetchCoefficient();

  /**
   * <pre>
   * Prefetching starts if there is less than prefetch_watermark fraction of full local bucket left.
   * Default value is inherited from parent or 0.75 for root.
   * Must be nonnegative and less than or equal to 1.
   * </pre>
   *
   * <code>double prefetch_watermark = 4;</code>
   */
  double getPrefetchWatermark();
}
