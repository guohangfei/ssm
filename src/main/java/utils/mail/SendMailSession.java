// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
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

import jodd.mail.Email;
import jodd.mail.EmailAddress;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailMessage;
import jodd.mail.EmailUtil;
import jodd.mail.MailException;
import jodd.mail.MailSession;
import jodd.util.StringPool;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates {@link jodd.mail.Email} sending session. Prepares and sends {@link #sendMail(jodd.mail.Email)} {@link jodd.mail.Email}s.
 */
public class SendMailSession extends jodd.mail.MailSession<Transport> {

	private static final String ALTERNATIVE = "alternative";
	private static final String RELATED = "related";
	private static final String CHARSET = ";charset=";
	private static final String INLINE = "inline";

	static {
		EmailUtil.setupSystemMailProperties();
	}

	/**
	 * Creates new mail session.
	 *
	 * @param session   {@link Session}
	 * @param transport {@link Transport}
	 */
	public SendMailSession(final Session session, final Transport transport) {
		super(session, transport);
	}

	@Override
	public Transport getService() {
		return (Transport) service;
	}

	/**
	 * Prepares message and sends it. Returns Message ID of sent email.
	 *
	 * @param email {@link jodd.mail.Email} to send.
	 * @return String representing message ID.
	 */
	public String sendMail(final jodd.mail.Email email) {
		try {
			final MimeMessage msg = createMessage(email);
			getService().sendMessage(msg, msg.getAllRecipients());
			return msg.getMessageID();
		} catch (final MessagingException msgexc) {
			throw new MailException("Failed to send email: " + email, msgexc);
		}
	}

	// ---------------------------------------------------------------- adapter

	/**
	 * Creates new {@link MimeMessage} from an {@link jodd.mail.Email}.
	 *
	 * @param email {@link jodd.mail.Email} to be created as a {@link MimeMessage}.
	 * @return {@link MimeMessage} created from an {@link jodd.mail.Email}.
	 * @throws MessagingException if there is a failure
	 */
	protected MimeMessage createMessage(final jodd.mail.Email email) throws MessagingException {
		final jodd.mail.Email clone = email.clone();

		final MimeMessage newMsg = new MimeMessage(getSession());

		setPeople(clone, newMsg);
		setSubject(clone, newMsg);
		setSentDate(clone, newMsg);
		setHeaders(clone, newMsg);
		addBodyData(clone, newMsg);
		return newMsg;
	}

	/**
	 * Sets subject in msgToSet from subject in emailWithData.
	 *
	 * @param emailWithData {@link jodd.mail.Email} with data
	 * @param msgToSet      {@link MimeMessage} to set data into.
	 * @throws MessagingException if there is a failure
	 */
	private void setSubject(final jodd.mail.Email emailWithData, final MimeMessage msgToSet) throws MessagingException {
		if (emailWithData.subjectEncoding() != null) {
			msgToSet.setSubject(emailWithData.subject(), emailWithData.subjectEncoding());
		} else {
			msgToSet.setSubject(emailWithData.subject());
		}
	}

	/**
	 * Sets sent date in msgToSet with sent date from emailWithData.
	 *
	 * @param emailWithData {@link jodd.mail.Email} with data
	 * @param msgToSet      {@link MimeMessage} to set data into.
	 * @throws MessagingException if there is a failure
	 */
	private void setSentDate(final jodd.mail.Email emailWithData, final MimeMessage msgToSet) throws MessagingException {
		Date date = emailWithData.sentDate();
		if (date == null) {
			date = new Date();
		}
		msgToSet.setSentDate(date);
	}

	/**
	 * Sets headers in msgToSet with headers from emailWithData.
	 *
	 * @param emailWithData {@link jodd.mail.Email} with data
	 * @param msgToSet      {@link MimeMessage} to set data into.
	 * @throws MessagingException if there is a failure
	 */
	private void setHeaders(final jodd.mail.Email emailWithData, final MimeMessage msgToSet) throws MessagingException {
		final Map<String, String> headers = emailWithData.headers();
		if (headers != null) {
			for (final Map.Entry<String, String> entry : headers.entrySet()) {
				msgToSet.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Sets FROM, REPLY-TO and recipients.
	 *
	 * @param emailWithData {@link jodd.mail.Email} with data
	 * @param msgToSet      {@link MimeMessage} to set data into.
	 * @throws MessagingException if there is a failure
	 */
	private void setPeople(final jodd.mail.Email emailWithData, final MimeMessage msgToSet) throws MessagingException {
		msgToSet.setFrom(emailWithData.from().toInternetAddress());
		msgToSet.setReplyTo(jodd.mail.EmailAddress.convert(emailWithData.replyTo()));
		setRecipients(emailWithData, msgToSet);
	}

	/**
	 * Sets TO, CC and BCC in msgToSet with TO, CC and BCC from emailWithData.
	 *
	 * @param emailWithData {@link jodd.mail.Email} with data
	 * @param msgToSet      {@link MimeMessage} to set data into.
	 * @throws MessagingException if there is a failure.
	 */
	private void setRecipients(final jodd.mail.Email emailWithData, final MimeMessage msgToSet) throws MessagingException {
		// TO
		final InternetAddress[] to = jodd.mail.EmailAddress.convert(emailWithData.to());
		if (to.length > 0) {
			msgToSet.setRecipients(RecipientType.TO, to);
		}

		// CC
		final InternetAddress[] cc = jodd.mail.EmailAddress.convert(emailWithData.cc());
		if (cc.length > 0) {
			msgToSet.setRecipients(RecipientType.CC, cc);
		}

		// BCC
		final InternetAddress[] bcc = EmailAddress.convert(emailWithData.bcc());
		if (bcc.length > 0) {
			msgToSet.setRecipients(RecipientType.BCC, bcc);
		}
	}

	/**
	 * Adds message data and attachments.
	 *
	 * @param emailWithData {@link jodd.mail.Email} with data
	 * @param msgToSet      {@link MimeMessage} to set data into.
	 * @throws MessagingException if there is a failure.
	 */
	private void addBodyData(final Email emailWithData, final MimeMessage msgToSet) throws MessagingException {
		final List<jodd.mail.EmailMessage> messages = emailWithData.messages();

		final int totalMessages = messages.size();

		// Need to use new list since filterEmbeddedAttachments(List) removes attachments from the source List
		final List<jodd.mail.EmailAttachment<? extends DataSource>> attachments = new ArrayList<>(emailWithData.attachments());

		if (attachments.isEmpty() && totalMessages == 1) {
			// special case: no attachments and just one content
			setContent(messages.get(0), msgToSet);

		} else {
			final MimeMultipart multipart = new MimeMultipart();

			if (totalMessages > 1) {
				final MimeMultipart msgMultipart = new MimeMultipart(ALTERNATIVE);
				multipart.addBodyPart(getBaseBodyPart(msgMultipart));
				for (final jodd.mail.EmailMessage emailMessage : messages) {
					msgMultipart.addBodyPart(getBodyPart(emailMessage, attachments));
				}
			}

			addAnyAttachments(attachments, multipart);

			msgToSet.setContent(multipart);
		}
	}

	/**
	 * Returns new {@link MimeBodyPart} with content set as msgMultipart.
	 *
	 * @param msgMultipart {@link MimeMultipart} to add to the new {@link MimeBodyPart}.
	 * @return new {@link MimeBodyPart} with content set as msgMultipart.
	 * @throws MessagingException if there is a failure.
	 */
	private MimeBodyPart getBaseBodyPart(final MimeMultipart msgMultipart) throws MessagingException {
		final MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(msgMultipart);
		return bodyPart;
	}

	/**
	 * @param emailMessage {@link jodd.mail.EmailMessage} with data.
	 * @param attachments  {@link List} of {@link jodd.mail.EmailAttachment}s.
	 * @return new {@link MimeBodyPart} with data from emailMessage and attachments.
	 * @throws MessagingException if there is a failure.
	 */
	private MimeBodyPart getBodyPart(final jodd.mail.EmailMessage emailMessage, final List<jodd.mail.EmailAttachment<? extends DataSource>> attachments) throws MessagingException {

		final MimeBodyPart bodyPart = new MimeBodyPart();

		// detect embedded attachments
		final List<jodd.mail.EmailAttachment<? extends DataSource>> embeddedAttachments = filterEmbeddedAttachments(attachments, emailMessage);

		if (embeddedAttachments.isEmpty()) {
			// no embedded attachments, just add message
			setContent(emailMessage, bodyPart);
		} else {
			attachments.removeAll(embeddedAttachments);

			// embedded attachments detected, join them as related
			final MimeMultipart relatedMultipart = new MimeMultipart(RELATED);

			final MimeBodyPart messageData = new MimeBodyPart();

			setContent(emailMessage, messageData);

			relatedMultipart.addBodyPart(messageData);

			addAnyAttachments(embeddedAttachments, relatedMultipart);

			bodyPart.setContent(relatedMultipart);
		}

		return bodyPart;
	}

	/**
	 * Sets emailWithData content into msgToSet.
	 *
	 * @param emailWithData {@link jodd.mail.EmailMessage} with data.
	 * @param partToSet     {@link Part} to set data into.
	 * @throws MessagingException if there is a failure.
	 */
	private void setContent(final jodd.mail.EmailMessage emailWithData, final Part partToSet) throws MessagingException {
		partToSet.setContent(emailWithData.getContent(), emailWithData.getMimeType() + CHARSET + emailWithData.getEncoding());
	}

	/**
	 * Creates attachment body part. Handles regular and inline attachments.
	 *
	 * @param attachment Body part {@link jodd.mail.EmailAttachment}.
	 * @return {@link MimeBodyPart} which represents body part attachment.
	 * @throws MessagingException if there is a failure.
	 */
	protected MimeBodyPart createAttachmentBodyPart(final jodd.mail.EmailAttachment<? extends DataSource> attachment) throws MessagingException {
		final MimeBodyPart part = new MimeBodyPart();

		final String attachmentName = attachment.getEncodedName();
		if (attachmentName != null) {
			part.setFileName(attachmentName);
		}

		part.setDataHandler(new DataHandler(attachment.getDataSource()));

		if (attachment.getContentId() != null) {
			part.setContentID(StringPool.LEFT_CHEV + attachment.getContentId() + StringPool.RIGHT_CHEV);
		}
		if (attachment.isInline()) {
			part.setDisposition(INLINE);
		}

		return part;
	}

	/**
	 * Filters out the {@link List} of embedded {@link jodd.mail.EmailAttachment}s for given {@link jodd.mail.EmailMessage}.
	 * This will remove the embedded attachments from the {@link List} and return them in a new {@link List}.
	 *
	 * @param attachments  {@link List} of attachments to search for in emailMessage.
	 * @param emailMessage {@link jodd.mail.EmailMessage} to see if attachment is embedded into.
	 * @return {@link List} of embedded {@link jodd.mail.EmailAttachment}s; otherwise, returns empty {@link List}.
	 */
	protected List<jodd.mail.EmailAttachment<? extends DataSource>> filterEmbeddedAttachments(final List<jodd.mail.EmailAttachment<? extends DataSource>> attachments, final EmailMessage emailMessage) {
		final List<jodd.mail.EmailAttachment<? extends DataSource>> embeddedAttachments = new ArrayList<>();

		if (attachments == null || attachments.isEmpty() || emailMessage == null) {
			return embeddedAttachments;
		}

		final Iterator<jodd.mail.EmailAttachment<? extends DataSource>> iterator = attachments.iterator();

		while (iterator.hasNext()) {
			final jodd.mail.EmailAttachment<? extends DataSource> emailAttachment = iterator.next();

			if (emailAttachment.isEmbeddedInto(emailMessage)) {
				embeddedAttachments.add(emailAttachment);
				iterator.remove();
			}
		}

		return embeddedAttachments;
	}

	/**
	 * Adds {@link List} of {@link jodd.mail.EmailAttachment}s to multipart.
	 *
	 * @param attachments {@link List} of {@link jodd.mail.EmailAttachment}s to add to multipart. This can be {@code null}.
	 * @param multipart   {@link MimeMultipart} to set data into.
	 * @throws MessagingException if there is a failure.
	 */
	private void addAnyAttachments(final List<jodd.mail.EmailAttachment<? extends DataSource>> attachments, final MimeMultipart multipart) throws MessagingException {
		for (final EmailAttachment<? extends DataSource> attachment : attachments) {
			final MimeBodyPart bodyPart = createAttachmentBodyPart(attachment);
			multipart.addBodyPart(bodyPart);
		}
	}

}