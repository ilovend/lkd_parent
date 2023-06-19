package com.lkd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.lkd.dao.OrderDao;
import com.lkd.entity.OrderEntity;
import com.lkd.service.OrderService;
import com.lkd.vo.OrderVO;
import com.lkd.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private RestHighLevelClient esClient;

    @Override
    public OrderEntity getByOrderNo(String orderNo) {
        QueryWrapper<OrderEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(OrderEntity::getOrderNo,orderNo);
        return this.getOne(qw);
    }


    @Override
    public Pager<OrderVO> search(Integer pageIndex, Integer pageSize, String orderNo, String openId, String startDate, String endDate) {
        return Pager.buildEmpty();
    }


    @Override
    public List<Long> getTop10Sku(Integer businessId) {

        SearchRequest searchRequest=new SearchRequest("order");
        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
        //查询条件：最近三个月

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("update_time");
        rangeQueryBuilder.gte( LocalDateTime.now().plusMonths(-3).format(  DateTimeFormatter.ISO_DATE_TIME )  );
        rangeQueryBuilder.lte( LocalDateTime.now().format(  DateTimeFormatter.ISO_DATE_TIME )  );

        BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
        boolQueryBuilder.must( rangeQueryBuilder );

        boolQueryBuilder.must( QueryBuilders.termQuery("business_id",businessId) );
        sourceBuilder.query(boolQueryBuilder);

        AggregationBuilder orderAgg = AggregationBuilders.terms("sku").field("sku_id")
                .subAggregation(AggregationBuilders.count("count").field("sku_id"))
                .order(BucketOrder.aggregation("count", false))
                .size(10);

        sourceBuilder.aggregation(orderAgg);
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            if(aggregations==null ) return  Lists.newArrayList();

            var term = (Terms)aggregations.get("sku");
            var buckets = term.getBuckets();

            return buckets.stream().map(  b->   Long.valueOf( b.getKey().toString() ) ).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }

    }

}
