package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.bo.FileBO;
import io.github.talelin.latticy.mapper.FileMapper;
import io.github.talelin.latticy.model.FileDO;
import io.github.talelin.latticy.module.file.FileProperties;
import io.github.talelin.latticy.module.file.FileTypeEnum;
import io.github.talelin.latticy.module.file.UploadHandler;
import io.github.talelin.latticy.module.file.Uploader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("FileServiceImpl单元测试")
class FileServiceImplTest {

    @MockBean
    private FileMapper fileMapper;

    @MockBean
    private Uploader uploader;

    @MockBean
    private FileProperties fileProperties;

    @Autowired
    private FileServiceImpl fileService;

    @Nested
    @DisplayName("文件上传测试")
    class FileUploadTests {

        @Test
        @DisplayName("输入参数：包含新文件的MultiValueMap；预期结果：返回上传后的FileBO列表；测试点：上传新文件")
        void upload_WithNewFiles_ShouldReturnFileBOList() {
            MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();
            MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
            fileMap.add("file", mockFile);

            doAnswer(invocation -> {
                UploadHandler handler = invocation.getArgument(1);
                io.github.talelin.latticy.module.file.File file = new io.github.talelin.latticy.module.file.File();
                file.setName("test.txt");
                file.setMd5("abc123");
                file.setType("LOCAL");
                file.setPath("/upload/test.txt");
                file.setKey("file");

                handler.preHandle(file);
                handler.afterHandle(file);
                return null;
            }).when(uploader).upload(any(), any(UploadHandler.class));

            when(fileMapper.selectByMd5(anyString())).thenReturn(null);
            when(fileMapper.insert(any(FileDO.class))).thenReturn(1);
            when(fileProperties.getServePath()).thenReturn("/assets/upload");
            when(fileProperties.getDomain()).thenReturn("http://localhost:5000");

            System.setProperty("os.name", "Linux");

            List<FileBO> result = fileService.upload(fileMap);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            verify(uploader, times(1)).upload(any(), any(UploadHandler.class));
        }

        @Test
        @DisplayName("输入参数：包含重复文件的MultiValueMap；预期结果：直接返回已存在的文件信息；测试点：MD5相同的文件不上传")
        void upload_WithDuplicateFile_ShouldReturnExistingDirectly() {
            MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();
            MockMultipartFile mockFile = new MockMultipartFile("file", "existing.txt", "text/plain", "content".getBytes());
            fileMap.add("file", mockFile);

            FileDO existing = new FileDO();
            existing.setId(1);
            existing.setName("existing.txt");
            existing.setMd5("abc123");
            existing.setType("LOCAL");
            existing.setPath("/upload/existing.txt");

            doAnswer(invocation -> {
                UploadHandler handler = invocation.getArgument(1);
                io.github.talelin.latticy.module.file.File file = new io.github.talelin.latticy.module.file.File();
                file.setMd5("abc123");
                file.setKey("existing.txt");
                handler.preHandle(file);
                return null;
            }).when(uploader).upload(any(), any(UploadHandler.class));

            when(fileMapper.selectByMd5(anyString())).thenReturn(existing);
            when(fileProperties.getServePath()).thenReturn("/assets/upload");
            when(fileProperties.getDomain()).thenReturn("http://localhost:5000");
            System.setProperty("os.name", "Linux");

            List<FileBO> result = fileService.upload(fileMap);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getKey()).isEqualTo("existing.txt");
        }

        @Test
        @DisplayName("输入参数：包含远程文件的MultiValueMap；预期结果：远程文件直接使用path作为URL；测试点：远程文件URL处理")
        void upload_WithRemoteFile_ShouldUsePathAsUrl() {
            MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();
            MockMultipartFile mockFile = new MockMultipartFile("file", "remote.txt", "text/plain", "content".getBytes());
            fileMap.add("file", mockFile);

            FileDO remote = new FileDO();
            remote.setId(1);
            remote.setName("remote.txt");
            remote.setMd5("remote123");
            remote.setType(FileTypeEnum.REMOTE.getValue());
            remote.setPath("http://cdn.com/file.txt");

            doAnswer(invocation -> {
                UploadHandler handler = invocation.getArgument(1);
                io.github.talelin.latticy.module.file.File file = new io.github.talelin.latticy.module.file.File();
                file.setMd5("remote123");
                file.setKey("remote.txt");
                handler.preHandle(file);
                return null;
            }).when(uploader).upload(any(), any(UploadHandler.class));

            when(fileMapper.selectByMd5(anyString())).thenReturn(remote);

            List<FileBO> result = fileService.upload(fileMap);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getUrl()).isEqualTo("http://cdn.com/file.txt");
        }
    }

    @Nested
    @DisplayName("文件存在性检查测试")
    class FileExistenceTests {

        @Test
        @DisplayName("输入参数：存在的MD5；预期结果：返回true；测试点：文件已存在检查")
        void checkFileExistByMd5_WithExistingMd5_ShouldReturnTrue() {
            when(fileMapper.selectCountByMd5(eq("existing_md5"))).thenReturn(1);

            boolean result = fileService.checkFileExistByMd5("existing_md5");

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：不存在的MD5；预期结果：返回false；测试点：文件不存在检查")
        void checkFileExistByMd5_WithNonExistentMd5_ShouldReturnFalse() {
            when(fileMapper.selectCountByMd5(eq("nonexistent"))).thenReturn(0);

            boolean result = fileService.checkFileExistByMd5("nonexistent");

            assertThat(result).isFalse();
        }
    }
}
