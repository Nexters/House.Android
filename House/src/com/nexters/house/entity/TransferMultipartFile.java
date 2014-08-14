package com.nexters.house.entity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/* 
 * originalFilename, name : 파일이름
 * content : 파일
 * contentType : 파일 타입
 * size : 사이즈
 * isEmpty : 파일이 비어있는지 여부
 */

public class TransferMultipartFile implements Serializable {
	private String originalFilename;
	private String name;
	private byte[] content;
	private String contentType;
	private long size;
	private boolean isEmpty;
	
	public TransferMultipartFile(){ 	}
	
	public TransferMultipartFile(String originalFilename, String name,
			byte[] content, String contentType, long size, boolean isEmpty) {
		super();
		this.originalFilename = originalFilename;
		this.name = name;
		this.content = content;
		this.contentType = contentType;
		this.size = size;
		this.isEmpty = isEmpty;
	}

	public TransferMultipartFile(String originalFilename, String name,
			InputStream inputStream, String contentType, long size, boolean isEmpty) throws IOException {
		super();
		this.originalFilename = originalFilename;
		this.name = name;
		this.contentType = contentType;
		this.size = size;
		this.isEmpty = isEmpty;
		
		content = new byte[(int) (this.size + 1)];
		inputStream.read(content);
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}
	
	public String getContentType() {
		return this.contentType;
	}

//	public InputStream getInputStream() throws IOException {
//		return new ByteArrayInputStream(content);
//	}

	public String getName() {
		return this.name;
	}

	public String getOriginalFilename() {
		return this.originalFilename;
	}

	public long getSize() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.isEmpty;
	}

	public void transferTo(File dest) throws IOException, IllegalStateException {
		new FileOutputStream(dest).write(content);
	}
	
	public byte[] getContent() {
		return content;
	}
}
