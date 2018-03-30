package utils.lagarto.form;

import java.util.HashMap;
import java.util.Map;
import jodd.lagarto.Tag;
import jodd.lagarto.TagType;
import jodd.lagarto.TagWriter;
import jodd.lagarto.form.FormFieldResolver;
import jodd.mutable.MutableInteger;
import jodd.util.CharSequenceUtil;
import jodd.util.StringUtil;

public class FormProcessorVisitor extends TagWriter {
	private static final String INPUT = "input";
	private static final String SELECT = "select";
	private static final String OPTION = "option";
	private static final String TEXTAREA = "textarea";
	private static final String TYPE = "type";
	private static final String VALUE = "value";
	private static final String NAME = "name";
	private static final String TEXT = "text";
	private static final String HIDDEN = "hidden";
	private static final String IMAGE = "image";
	private static final String PASSWORD = "password";
	private static final String CHECKBOX = "checkbox";
	private static final String TRUE = "true";
	private static final String CHECKED = "checked";
	private static final String RADIO = "radio";
	private static final String SELECTED = "selected";
	private final FormFieldResolver resolver;
	protected Map<String, MutableInteger> valueNameIndexes;
	private boolean inSelect;
	private String currentSelectName;
	private String textAreaValue;
	private boolean inTextArea;

	public FormProcessorVisitor(Appendable appendable, FormFieldResolver resolver) {
		super(appendable);
		this.resolver = resolver;
	}

	public void tag(Tag tag) {
		if (tag.getType().isStartingTag()) {
			if (CharSequenceUtil.equalsToLowercase(tag.getName(), "input")) {
				this.processInputStartTag(tag);
				super.tag(tag);
				return;
			}

			if (this.inSelect && CharSequenceUtil.equalsToLowercase(tag.getName(), "option")) {
				this.processOptionOpenTag(tag);
				super.tag(tag);
				return;
			}
		}

		if (tag.getType() == TagType.START) {
			if (CharSequenceUtil.equalsToLowercase(tag.getName(), "textarea")) {
				this.processTextareaStartTag(tag);
			} else if (CharSequenceUtil.equalsToLowercase(tag.getName(), "select")) {
				this.processSelectOpenTag(tag);
			}
		} else if (tag.getType() == TagType.END) {
			if (this.inTextArea && CharSequenceUtil.equalsToLowercase(tag.getName(), "textarea")) {
				this.processTextareaEndTag();
			} else if (this.inSelect && CharSequenceUtil.equalsToLowercase(tag.getName(), "select")) {
				this.processSelectEndTag();
			}
		}

		super.tag(tag);
	}

	public void text(CharSequence text) {
		if (!this.inTextArea) {
			super.text(text);
		}
	}

	private void processInputStartTag(Tag tag) {
		CharSequence tagType = tag.getAttributeValue("type");
		if (tagType != null) {
			CharSequence nameSequence = tag.getAttributeValue("name");
			if (nameSequence != null) {
				String name = nameSequence.toString();
				Object valueObject = this.resolver.value(name);
				if (valueObject != null) {
					String tagTypeName = tagType.toString().toLowerCase();
					String tagValue;
					if (!tagTypeName.equals("text") && !tagTypeName.equals("hidden") && !tagTypeName.equals("image")
							&& !tagTypeName.equals("password")) {
						if (tagTypeName.equals("checkbox")) {
							Object arg12 = tag.getAttributeValue("value");
							if (arg12 == null) {
								arg12 = "true";
							}

							tagValue = ((CharSequence) arg12).toString();
							if (valueObject.getClass().isArray()) {
								String[] vs = StringUtil.toStringArray(valueObject);
								String[] arg8 = vs;
								int arg9 = vs.length;

								for (int arg10 = 0; arg10 < arg9; ++arg10) {
									String vsk = arg8[arg10];
									if (vsk != null && vsk.contentEquals(tagValue)) {
										tag.setAttribute("checked", (CharSequence) null);
									}
								}
							} else if (tagValue.equals(valueObject.toString())) {
								tag.setAttribute("checked", (CharSequence) null);
							}
						} else if (tagType.equals("radio")) {
							CharSequence arg13 = tag.getAttributeValue("value");
							if (arg13 != null) {
								tagValue = arg13.toString();
								if (tagValue.equals(valueObject.toString())) {
									tag.setAttribute("checked", (CharSequence) null);
								}
							}
						}
					} else {
						tagValue = this.valueToString(name, valueObject);
						if (tagValue == null) {
							return;
						}

						tag.setAttribute("value", tagValue);
					}

				}
			}
		}
	}

	protected String valueToString(String name, Object valueObject) {
		if (!valueObject.getClass().isArray()) {
			return valueObject.toString();
		} else {
			String[] array = (String[]) ((String[]) valueObject);
			if (this.valueNameIndexes == null) {
				this.valueNameIndexes = new HashMap();
			}

			MutableInteger index = (MutableInteger) this.valueNameIndexes.get(name);
			if (index == null) {
				index = new MutableInteger(0);
				this.valueNameIndexes.put(name, index);
			}

			if (index.value >= array.length) {
				return null;
			} else {
				String result = array[index.value];
				++index.value;
				return result;
			}
		}
	}

	private void processSelectOpenTag(Tag tag) {
		CharSequence name = tag.getAttributeValue("name");
		if (name != null) {
			this.currentSelectName = name.toString();
			this.inSelect = true;
		}
	}

	private void processSelectEndTag() {
		this.inSelect = false;
		this.currentSelectName = null;
	}

	private void processOptionOpenTag(Tag tag) {
		CharSequence tagValue = tag.getAttributeValue("value");
		if (tagValue != null) {
			Object vals = this.resolver.value(this.currentSelectName);
			if (vals != null) {
				String arg8 = tagValue.toString();
				if (vals.getClass().isArray()) {
					String[] value = StringUtil.toStringArray(vals);
					String[] arg4 = value;
					int arg5 = value.length;

					for (int arg6 = 0; arg6 < arg5; ++arg6) {
						String vsk = arg4[arg6];
						if (vsk != null && vsk.contentEquals(arg8)) {
							tag.setAttribute("selected", (CharSequence) null);
						}
					}
				} else {
					String arg9 = StringUtil.toString(vals);
					if (arg9.contentEquals(arg8)) {
						tag.setAttribute("selected", (CharSequence) null);
					}
				}

			}
		}
	}

	private void processTextareaStartTag(Tag tag) {
		this.inTextArea = true;
		CharSequence name = tag.getAttributeValue("name");
		if (name != null) {
			Object valueObject = this.resolver.value(name.toString());
			if (valueObject != null) {
				this.textAreaValue = valueObject.toString();
			}

		}
	}

	private void processTextareaEndTag() {
		this.inTextArea = false;
		if (this.textAreaValue != null) {
			super.text(this.textAreaValue);
			this.textAreaValue = null;
		}
	}
}