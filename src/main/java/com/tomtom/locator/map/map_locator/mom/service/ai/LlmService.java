package com.tomtom.locator.map.map_locator.mom.service.ai;

import java.util.List;

public interface LlmService {
    String getNameForPoiNames(List<String> names);
}
