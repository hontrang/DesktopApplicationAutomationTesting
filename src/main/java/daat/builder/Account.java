package daat.builder;

import daat.helper.GoogleDriverHelper;

public class Account {
    protected String accountName;
    protected String fileName;
    protected String currentCode;
    protected String newCode;

    protected GoogleDriverHelper drive;

    private Account() {
    }

    protected Account(AccountBuilder builder) {
        this.accountName = builder.accountName;
        this.fileName = builder.fileName;
        this.currentCode = builder.currentCode;
        this.newCode = builder.newCode;
        this.drive = builder.drive;
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public String getAccountName() {
        return accountName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCurrentCode() {
        return currentCode;
    }

    public void setCurrentCode(String currentCode) {
        this.currentCode = currentCode;
    }


    public String getNewCode() {
        return newCode;
    }

    public GoogleDriverHelper getGoogleDriveInstance() {
        return drive;
    }

    public static class AccountBuilder {
        protected String accountName;
        protected String fileName;
        protected String currentCode;
        protected String newCode;
        protected GoogleDriverHelper drive;

        protected AccountBuilder self() {
            return this;
        }

        public AccountBuilder accountName(String accountName) {
            this.accountName = accountName;
            return self();
        }

        public AccountBuilder fileName(String fileName) {
            this.fileName = fileName;
            return self();
        }

        public AccountBuilder currentCode(String currentCode) {
            this.currentCode = currentCode;
            return self();
        }

        public AccountBuilder newCode(String newCode) {
            this.newCode = newCode;
            return self();
        }

        public AccountBuilder googleDriveInstance(GoogleDriverHelper drive) {
            this.drive = drive;
            return self();
        }

        public Account build() {
            return new Account(this);
        }

    }

}
