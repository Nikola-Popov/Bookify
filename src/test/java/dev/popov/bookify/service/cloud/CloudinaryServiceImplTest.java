package dev.popov.bookify.service.cloud;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;

import dev.popov.bookify.commons.FileFactory;

@RunWith(MockitoJUnitRunner.class)
public class CloudinaryServiceImplTest {
	private static final String ORIGINAL_FILENAME = "originalFilename";
	private static final String URL = "url";
	private static final String IMAGE_URL = "imageUrl";

	@InjectMocks
	private CloudinaryServiceImpl cloudinaryService;

	@Mock
	private Cloudinary cloudinaryMock;

	@Mock
	private MultipartFile multipartFileMock;

	@Mock
	private File fileMock;

	@Mock
	private Uploader uploaderMock;

	@Mock
	private Map<String, Object> uploadPropertiesMock;

	@Mock
	private FileFactory fileFactoryMock;

	@Before
	public void setUp() throws Exception {
		when(multipartFileMock.getOriginalFilename()).thenReturn(ORIGINAL_FILENAME);
		when(cloudinaryMock.uploader()).thenReturn(uploaderMock);
		when(uploaderMock.upload(eq(fileMock), anyMap())).thenReturn(uploadPropertiesMock);
		when(uploadPropertiesMock.get(URL)).thenReturn(IMAGE_URL);
		when(fileFactoryMock.createTempFile(ORIGINAL_FILENAME)).thenReturn(fileMock);
	}

	@Test(expected = IOException.class)
	public void testUploadImageThrowsExceptionWhenTransferingFromMultipartFileToTempFile() throws Exception {
		doThrow(IOException.class).when(multipartFileMock).transferTo(fileMock);

		cloudinaryService.uploadImage(multipartFileMock);
	}

	@Test
	public void testUploadImageUploadsCorrectlyAndReturnsImageURL() throws Exception {
		assertThat(cloudinaryService.uploadImage(multipartFileMock), equalTo(IMAGE_URL));

		verify(multipartFileMock).transferTo(fileMock);
		verify(cloudinaryMock).uploader();
		verify(uploaderMock).upload(eq(fileMock), anyMap());
		verify(uploadPropertiesMock).get(URL);
	}

}
