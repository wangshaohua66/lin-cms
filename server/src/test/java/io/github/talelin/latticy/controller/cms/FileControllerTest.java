package io.github.talelin.latticy.controller.cms;

import io.github.talelin.latticy.bo.FileBO;
import io.github.talelin.latticy.service.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("FileController单元测试")
class FileControllerTest {

    @MockBean
    private FileService fileService;

    @MockBean
    private MultipartHttpServletRequest multipartRequest;

    @Autowired
    private FileController fileController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    @DisplayName("输入参数：MultipartHttpServletRequest包含文件；预期结果：返回FileBO列表；测试点：文件上传")
    void upload_WithFiles_ShouldReturnFileBOList() {
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", "text/plain", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", "text/plain", "content2".getBytes());

        MultiValueMap<String, MultipartFile> fileMap = new LinkedMultiValueMap<>();
        fileMap.put("file", Arrays.asList(file1, file2));

        FileBO bo1 = new FileBO();
        bo1.setId(1);
        bo1.setKey("test1.txt");
        bo1.setPath("/upload/test1.txt");
        FileBO bo2 = new FileBO();
        bo2.setId(2);
        bo2.setKey("test2.txt");
        bo2.setPath("/upload/test2.txt");

        when(multipartRequest.getMultiFileMap()).thenReturn(fileMap);
        when(fileService.upload(any())).thenReturn(Arrays.asList(bo1, bo2));

        List<FileBO> result = fileController.upload(multipartRequest);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getKey()).isEqualTo("test1.txt");
        verify(fileService, times(1)).upload(any());
    }

    @Test
    @DisplayName("输入参数：空的MultipartHttpServletRequest；预期结果：返回空列表；测试点：无文件上传场景")
    void upload_WithNoFiles_ShouldReturnEmptyList() {
        MultiValueMap<String, MultipartFile> emptyMap = new LinkedMultiValueMap<>();

        when(multipartRequest.getMultiFileMap()).thenReturn(emptyMap);
        when(fileService.upload(any())).thenReturn(java.util.Collections.emptyList());

        List<FileBO> result = fileController.upload(multipartRequest);

        assertThat(result).isEmpty();
        verify(fileService, times(1)).upload(any());
    }
}
