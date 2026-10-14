package com.example.weblog.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "minio")
@Component
@Data
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    /**
     * 外网访问地址（用于生成返回给前端的文件访问 URL）
     * 若未配置，则默认使用 endpoint
     */
    private String externalEndpoint;
}



