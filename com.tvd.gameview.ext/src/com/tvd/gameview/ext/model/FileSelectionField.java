package com.tvd.gameview.ext.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class FileSelectionField {
	protected FileSelectionField() {}
	protected FileSelectionField(Builder pBuider) {
		this.mParent = pBuider.mParent;
		this.createFields();
		this.addListeners();
		this.mLabel.setText(pBuider.mLabelText);
		this.mLabel.setToolTipText(pBuider.mLabelToolTipText);
		this.mText.setToolTipText(pBuider.mTextToolTipText);
		this.mBrowseButton.setText(pBuider.mButtonText);
		this.mBrowseButton.setToolTipText(pBuider.mButtonToolTipText);
	}
	protected void createFields() {
		this.mLabel = new Label(mParent, SWT.NONE);
		this.mText = new Text(mParent, SWT.BORDER);
		this.mText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.mBrowseButton = new Button(mParent, SWT.NONE);
	}
	
	protected void addListeners() {
		this.mFileDialog = new FileDialog(mParent.getShell(), SWT.OPEN);
		this.mBrowseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				mText.setText(mFileDialog.open());
			}
		});
	}
	
	public Label getLabel() {
		return mLabel;
	}
	public void setLabel(Label pLabel) {
		this.mLabel = pLabel;
	}
	public Text getText() {
		return mText;
	}
	public void setText(Text pText) {
		this.mText = pText;
	}
	public Button getBrowseButton() {
		return mBrowseButton;
	}
	public void setBrowseButton(Button pBrowseButton) {
		this.mBrowseButton = pBrowseButton;
	}
	
	public static class Builder {
		private String mLabelText = "";
		private String mLabelToolTipText = "";
		private String mTextToolTipText = "";
		private String mButtonToolTipText = "";
		private String mButtonText = "Browse...";
		
		private Composite mParent;
		
		public Builder(Composite pParent) {
			this.mParent = pParent;
		}
		
		public Builder setLabelText(String pLabelText) {
			this.mLabelText = pLabelText;
			return this;
		}
		public Builder setLabelToolTipText(String mLabelToolTipText) {
			this.mLabelToolTipText = mLabelToolTipText;
			return this;
		}
		public Builder setTextToolTipText(String pTextToolTipText) {
			this.mTextToolTipText = pTextToolTipText;
			return this;
		}
		public Builder setButtonToolTipText(String pButtonToolTipText) {
			this.mButtonToolTipText = pButtonToolTipText;
			return this;
		}
		public Builder setButtonText(String pButtonText) {
			this.mButtonText = pButtonText;
			return this;
		}
		
		public FileSelectionField build() {
			return new FileSelectionField(this);
		}
	}
	
	protected Label mLabel;
	protected Text mText;
	protected Button mBrowseButton;
	private FileDialog mFileDialog;
	
	protected Composite mParent;
}
