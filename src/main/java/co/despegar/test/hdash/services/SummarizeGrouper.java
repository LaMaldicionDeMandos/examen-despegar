package co.despegar.test.hdash.services;

import co.despegar.test.hdash.model.Country;
import co.despegar.test.hdash.model.Hotel;
import co.despegar.test.hdash.model.Region;
import co.despegar.test.hdash.restclients.RegionRestClient;
import co.despegar.test.hdash.services.SummarizerService;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by boot on 03/06/2018.
 */
public class SummarizeGrouper {
    public static <T extends Region> Map<String, List<Hotel>> group(final List<Hotel> hotels, final SummarizerService summarizer,
                                                 final RegionRestClient<T> restClient) {
        final List<T> allRegions = restClient.findAll();
        final Map<String, List<Hotel>> childrenGroup = summarizer.group(hotels);
        final Map<String, List<Hotel>> regionsGroup = Maps.newHashMap();
        childrenGroup.keySet().stream().reduce(regionsGroup, (group, regionId) -> {
            T region = Iterables.tryFind(allRegions, (c) -> c.getId().equals(regionId)).orNull();
            if (!group.containsKey(region.getParentId())) {
                group.put(region.getParentId(), newArrayList(childrenGroup.get(regionId)));
            } else {
                group.get(region.getParentId()).addAll(childrenGroup.get(regionId));
            }
            return group;
        }, (group, group2) -> group);
        return regionsGroup;
    }
}
