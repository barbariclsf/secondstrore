package com.squirrel.util;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoodsRecommender {
    /**
     * fileDataModel为文件加载
     * mySQLDataModel为mysql数据库加载
     */
    @Resource(name = "fileDataModel")
    private DataModel dataModel;

    public List<Long> userBasedRecommend(long userID, int size) throws TasteException {
        UserSimilarity similarity  = new EuclideanDistanceSimilarity(dataModel );
        int NEIGHBORHOOD_NUM = 10;
        NearestNUserNeighborhood  neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, similarity, dataModel );
        Recommender recommender = new CachingRecommender(new GenericUserBasedRecommender(dataModel , neighbor, similarity));
        List<RecommendedItem> recommendations = recommender.recommend(userID, size);
        return recommendations.stream().map(m->m.getItemID()).collect(Collectors.toList());
    }

    public List<Long> itemBasedRecommend(long userID, int size) throws TasteException {
        ItemSimilarity itemSimilarity = new CityBlockSimilarity(dataModel);
        Recommender recommender = new CachingRecommender(new GenericBooleanPrefItemBasedRecommender(dataModel, itemSimilarity));
        List<RecommendedItem> recommendations = recommender.recommend(userID, size);
        return recommendations.stream().map(m->m.getItemID()).collect(Collectors.toList());
    }

}
