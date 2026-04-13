package io.github.talelin.latticy.common.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * JSON 数据存储管理器
 * 用于替代数据库，将数据存储在 JSON 文件中
 */
@Slf4j
@Component
public class JsonDataStore {

    private final ObjectMapper objectMapper;
    private final File dataDir;
    private final AtomicInteger idGenerator;

    public JsonDataStore() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.dataDir = new File("data");
        this.idGenerator = new AtomicInteger(1);
        init();
    }

    @PostConstruct
    public void init() {
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            log.info("创建数据目录: {}", dataDir.getAbsolutePath());
        }
    }

    /**
     * 获取数据文件
     */
    private File getDataFile(String entityName) {
        return new File(dataDir, entityName + ".json");
    }

    /**
     * 读取数据
     */
    public <T> List<T> readAll(String entityName, Class<T> clazz) {
        File file = getDataFile(entityName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("读取数据失败: {}", entityName, e);
            return new ArrayList<>();
        }
    }

    /**
     * 保存所有数据
     */
    public <T> void saveAll(String entityName, List<T> data) {
        File file = getDataFile(entityName);
        try {
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            log.error("保存数据失败: {}", entityName, e);
        }
    }

    /**
     * 插入数据
     */
    public <T extends JsonEntity> T insert(String entityName, T entity, Class<T> clazz) {
        List<T> list = readAll(entityName, clazz);
        entity.setId(idGenerator.getAndIncrement());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        list.add(entity);
        saveAll(entityName, list);
        return entity;
    }

    /**
     * 更新数据
     */
    public <T extends JsonEntity> T update(String entityName, T entity, Class<T> clazz) {
        List<T> list = readAll(entityName, clazz);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(entity.getId())) {
                entity.setUpdateTime(System.currentTimeMillis());
                list.set(i, entity);
                saveAll(entityName, list);
                return entity;
            }
        }
        return null;
    }

    /**
     * 删除数据（逻辑删除）
     */
    public <T extends JsonEntity> boolean delete(String entityName, Integer id, Class<T> clazz) {
        List<T> list = readAll(entityName, clazz);
        for (T entity : list) {
            if (entity.getId().equals(id)) {
                entity.setIsDeleted(true);
                entity.setDeleteTime(System.currentTimeMillis());
                saveAll(entityName, list);
                return true;
            }
        }
        return false;
    }

    /**
     * 物理删除
     */
    public <T extends JsonEntity> boolean physicalDelete(String entityName, Integer id, Class<T> clazz) {
        List<T> list = readAll(entityName, clazz);
        boolean removed = list.removeIf(entity -> entity.getId().equals(id));
        if (removed) {
            saveAll(entityName, list);
        }
        return removed;
    }

    /**
     * 根据ID查询
     */
    public <T extends JsonEntity> T selectById(String entityName, Integer id, Class<T> clazz) {
        return readAll(entityName, clazz).stream()
                .filter(e -> e.getId().equals(id) && !e.getIsDeleted())
                .findFirst()
                .orElse(null);
    }

    /**
     * 查询所有未删除的数据
     */
    public <T extends JsonEntity> List<T> selectAll(String entityName, Class<T> clazz) {
        return readAll(entityName, clazz).stream()
                .filter(e -> !e.getIsDeleted())
                .collect(Collectors.toList());
    }

    /**
     * 条件查询
     */
    public <T extends JsonEntity> List<T> selectByCondition(String entityName, Class<T> clazz, java.util.function.Predicate<T> condition) {
        return readAll(entityName, clazz).stream()
                .filter(e -> !e.getIsDeleted())
                .filter(condition)
                .collect(Collectors.toList());
    }

    /**
     * 分页查询
     */
    public <T extends JsonEntity> List<T> selectPage(String entityName, Class<T> clazz, int page, int size) {
        List<T> all = selectAll(entityName, clazz);
        int start = (page - 1) * size;
        int end = Math.min(start + size, all.size());
        if (start >= all.size()) {
            return new ArrayList<>();
        }
        return all.subList(start, end);
    }

    /**
     * 获取总数
     */
    public <T extends JsonEntity> long count(String entityName, Class<T> clazz) {
        return selectAll(entityName, clazz).size();
    }

    /**
     * 条件计数
     */
    public <T extends JsonEntity> long countByCondition(String entityName, Class<T> clazz, java.util.function.Predicate<T> condition) {
        return selectByCondition(entityName, clazz, condition).size();
    }
}
