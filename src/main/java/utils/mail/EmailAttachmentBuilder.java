// Copyright (c) 2003-present, utils Team (http://utils.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package utils.mail;

import utils.io.FileNameUtil;
import utils.io.FileUtil;
import utils.mail.EmailAttachment;
import utils.mail.EmailMessage;
import utils.mail.MailException;
import utils.util.net.MimeTypes;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static utils.mail.EmailUtil.NO_NAME;

/**
 * Helper class for convenient {@link utils.mail.EmailAttachment} creation.
 */
public class EmailAttachmentBuilder {

	// ---------------------------------------------------------------- constructor

	/**
	 * Only allow instantiation from {@link utils.mail.EmailAttachment} class
	 */
	protected EmailAttachmentBuilder() {
	}

	// ---------------------------------------------------------------- properties

	/**
	 * {@link String} with name of {@link utils.mail.EmailAttachment}.
	 */
	private String name;

	/**
	 * Content ID of {@link utils.mail.EmailAttachment}.
	 */
	private String contentId;

	/**
	 * Whether the {@link utils.mail.EmailAttachment} is inline. Defaults to false.
	 */
	private boolean isInline = false;

	/**
	 * {@link DataSource} containing {@link utils.mail.EmailAttachment} content.
	 */
	private DataSource dataSource;

	/**
	 * Target {@link utils.mail.EmailMessage}.
	 */
	private utils.mail.EmailMessage targetMessage;

	// ---------------------------------------------------------------- data

	/**
	 * Sets file name.
	 *
	 * @param name File name to set.
	 * @return this
	 */
	public EmailAttachmentBuilder name(final String name) {
		if (name != null && !name.trim().isEmpty()) {
			this.name = name;
		}
		return this;
	}

	/**
	 * Sets content ID.
	 *
	 * @param contentId content ID of {@link utils.mail.EmailAttachment}.
	 * @return this
	 */
	public EmailAttachmentBuilder contentId(final String contentId) {
		this.contentId = contentId;
		return this;
	}

	/**
	 * Sets whether {@link utils.mail.EmailAttachment} is inline.
	 *
	 * @param isInline {@code true} for inline.
	 * @return this
	 */
	public EmailAttachmentBuilder inline(final boolean isInline) {
		this.isInline = isInline;
		return this;
	}

	/**
	 * Sets target {@link utils.mail.EmailMessage}.
	 *
	 * @param targetMessage Target {@link utils.mail.EmailMessage}.
	 * @return this
	 */
	public EmailAttachmentBuilder embeddedMessage(final EmailMessage targetMessage) {
		this.targetMessage = targetMessage;
		return this;
	}

	/**
	 * Sets the {@link DataSource}. Common {@link DataSource}s include {@link ByteArrayDataSource} and
	 * {@link FileDataSource}.
	 *
	 * @param dataSource {@link DataSource}
	 * @return this
	 */
	public <T extends DataSource> EmailAttachmentBuilder content(final T dataSource) {
		this.dataSource = dataSource;
		name(dataSource.getName());
		return this;
	}

	/**
	 * Creates new {@link ByteArrayDataSource} and then calls {@link #content(DataSource)}.
	 *
	 * @param inputStream {@link InputStream}
	 * @param contentType content type from {@link utils.mail.EmailAttachment}.
	 * @return this
	 * @throws IOException if {@link ByteArrayDataSource} cannot be created from {@link InputStream}
	 * @see #content(DataSource)
	 */
	public EmailAttachmentBuilder content(final InputStream inputStream, final String contentType)
		throws IOException {
		return content(new ByteArrayDataSource(inputStream, resolveContentType(contentType)));
	}

	/**
	 * Creates new {@link ByteArrayDataSource} and then calls {@link #content(DataSource)}.
	 *
	 * @param bytes       array of bytes
	 * @param contentType content type from {@link utils.mail.EmailAttachment}.
	 * @return this
	 * @see #content(DataSource)
	 */
	public EmailAttachmentBuilder content(final byte[] bytes, final String contentType) {
		return content(new ByteArrayDataSource(bytes, resolveContentType(contentType)));
	}

	/**
	 * Uses {@code null} contentType.
	 *
	 * @see #content(byte[], String)
	 */
	public EmailAttachmentBuilder content(final byte[] bytes) {
		return content(bytes, null);
	}

	/**
	 * Creates new {@link FileDataSource} and then calls {@link #content(DataSource)}
	 *
	 * @param file {@link File}
	 * @return this
	 * @see #content(DataSource)
	 */
	public EmailAttachmentBuilder content(final File file) {
		return content(new FileDataSource(file));
	}

	/**
	 * @param fileName String representing file name.
	 * @return this
	 * @see #content(File)
	 */
	public EmailAttachmentBuilder content(final String fileName) {
		return content(new File(fileName));
	}

	// ---------------------------------------------------------------- factory/builder

	/**
	 * Creates {@link utils.mail.EmailAttachment}.
	 *
	 * @return {@link utils.mail.EmailAttachment}.
	 * @throws utils.mail.MailException if issue with {@link DataSource}.
	 */
	public utils.mail.EmailAttachment<ByteArrayDataSource> buildByteArrayDataSource() throws utils.mail.MailException {
		try {
			final ByteArrayDataSource bads;
			if (dataSource instanceof ByteArrayDataSource) {
				bads = (ByteArrayDataSource) dataSource;
			} else {
				bads = new ByteArrayDataSource(dataSource.getInputStream(), dataSource.getContentType());
			}
			checkDataSource();
			return new utils.mail.EmailAttachment<>(name, contentId, isInline, bads).setEmbeddedMessage(targetMessage);
		} catch (final IOException ioexc) {
			throw new utils.mail.MailException(ioexc);
		}
	}

	/**
	 * Creates {@link utils.mail.EmailAttachment}.
	 *
	 * @return {@link utils.mail.EmailAttachment}.
	 * @throws utils.mail.MailException if issue with {@link DataSource}.
	 */
	public utils.mail.EmailAttachment<FileDataSource> buildFileDataSource() throws utils.mail.MailException {
		try {
			final FileDataSource fds;
			if (dataSource instanceof FileDataSource) {
				fds = (FileDataSource) dataSource;
			} else {
				final File file = new File(dataSource.getName());
				FileUtil.writeStream(file, dataSource.getInputStream());
				fds = new FileDataSource(file);
			}
			checkDataSource();
			return new EmailAttachment<>(name, contentId, isInline, fds).setEmbeddedMessage(targetMessage);
		} catch (final IOException ioexc) {
			throw new utils.mail.MailException(ioexc);
		}
	}

	/**
	 * Check to ensure {@link DataSource} ds is valid.
	 *
	 * @throws utils.mail.MailException if DataSource is {@code null}.
	 */
	private void checkDataSource() {
		if (dataSource == null) {
			throw new MailException("dataSource must be valid. It can be set using #content().");
		}
	}

	// ---------------------------------------------------------------- properties

	/**
	 * Set content ID if it is missing.
	 *
	 * @return this
	 * @see #contentId(String)
	 */
	protected EmailAttachmentBuilder setContentIdFromNameIfMissing() {
		if (contentId == null) {
			if (name != null) {
				contentId(FileNameUtil.getName(name));
			} else {
				contentId(NO_NAME);
			}
		}
		return this;
	}

	/**
	 * Resolves content type from all data.
	 *
	 * @param contentType Content type if we know it. {@code null} is fine to use.
	 * @return content type
	 */
	protected String resolveContentType(final String contentType) {
		if (contentType != null) {
			return contentType;
		}
		if (name == null) {
			return MimeTypes.MIME_APPLICATION_OCTET_STREAM;
		}

		final String extension = FileNameUtil.getExtension(name);
		return MimeTypes.getMimeType(extension);
	}

}