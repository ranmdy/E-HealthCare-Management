package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RegistrationAndLoginPage extends Application {

    // Style constants for toggles
    private final String ACTIVE_TOGGLE_STYLE = "-fx-background-color: white; -fx-background-radius: 6; -fx-text-fill: #0052cc; -fx-font-weight: bold; -fx-cursor: hand;";
    private final String INACTIVE_TOGGLE_STYLE = "-fx-background-color: transparent; -fx-text-fill: #7a869a; -fx-font-weight: bold; -fx-cursor: hand;";

    // Dynamic layout containers & controls
    private VBox dynamicFieldsContainer;
    private Label welcome;
    private Button actionBtn;
    private Button loginBtn;
    private Button registerBtn;

    // Class-level references for Login Fields
    private TextField loginEmailField;
    private PasswordField loginPasswordField;

    // Class-level references for Patient Input Fields
    private TextField pFullNameField;
    private TextField pDobField;
    private TextField pContactField;
    private TextField pEmailField;
    private PasswordField pPasswordField;

    // Class-level references for Doctor Input Fields
    private TextField dFullNameField;
    private TextField dLicenseField;
    private TextField dSpecialtyField;
    private TextField dCategoryTypeField;
    private TextField dEmailField;
    private PasswordField dPasswordField;

    // Class-level references for Hospital Input Fields
    private TextField hNameField;
    private ComboBox<String> hBranchDropdown;
    private TextField hAdminIdField;
    private TextField hEmailField;
    private PasswordField hPasswordField;

    // State trackers
    private boolean isRegisterMode = true;
    private String currentRole = "Patient";

    // Nigerian Valid Carrier Prefixes Mapped Registry
    private static final Set<String> VALID_NIGERIAN_PREFIXES = new HashSet<>(Arrays.asList(
            // MTN
            "701", "702", "703", "704", "706", "803", "806", "810", "813", "814", "816", "903", "906", "913", "916",
            // Airtel
            "708", "802", "808", "812", "902", "907", "912",
            // Glo
            "705", "805", "807", "811", "815", "905", "915",
            // 9mobile
            "809", "817", "818", "909", "919"
    ));

    @Override
    public void start(Stage primaryStage) {
        // Root Layout
        HBox root = new HBox();
        root.setPrefSize(1200, 750);

        // Left Pane Layout
        VBox leftPane = new VBox(20);
        leftPane.setStyle("-fx-background-color: #172b4d; -fx-padding: 80 60 80 60;");
        leftPane.setPrefWidth(550);
        HBox.setHgrow(leftPane, Priority.NEVER);

        Label title = new Label("Care that travels to\nyou.");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 42));
        title.setWrapText(true);

        Label subTitle = new Label("Connect with licensed doctors remotely. Consultations,\nprescriptions, and hospital admissions, all in one place.");
        subTitle.setTextFill(Color.web("#a5b2c6"));
        subTitle.setFont(Font.font("System", 16));
        subTitle.setWrapText(true);
        VBox.setMargin(subTitle, new Insets(0, 0, 20, 0));

        VBox bulletPoints = new VBox(15);
        bulletPoints.getChildren().addAll(
                createBulletPoint("Symptoms routed to the right specialist, automatically"),
                createBulletPoint("Live chat, e-prescriptions and bed admission"),
                createBulletPoint("Patient, doctor and hospital. One connected system")
        );

        leftPane.getChildren().addAll(title, subTitle, bulletPoints);

        // Right Pane Layout
        StackPane rightPaneContainer = new StackPane();
        rightPaneContainer.setStyle("-fx-background-color: #fbfbfa;");
        HBox.setHgrow(rightPaneContainer, Priority.ALWAYS);

        VBox formBox = new VBox(15);
        formBox.setMaxWidth(450);
        formBox.setAlignment(Pos.CENTER_LEFT);

        welcome = new Label("Welcome");
        welcome.setFont(Font.font("System", FontWeight.BOLD, 28));
        welcome.setTextFill(Color.web("#172b4d"));

        Label signinSub = new Label("Sign in or create an account to continue.");
        signinSub.setTextFill(Color.web("#7a869a"));
        signinSub.setFont(Font.font("System", 14));
        VBox.setMargin(signinSub, new Insets(-10, 0, 15, 0));

        HBox toggleBox = new HBox();
        toggleBox.setStyle("-fx-background-color: #f1f2f4; -fx-background-radius: 8; -fx-padding: 5;");
        toggleBox.setPrefHeight(45);

        loginBtn = new Button("Log in");
        loginBtn.setStyle(INACTIVE_TOGGLE_STYLE);
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(loginBtn, Priority.ALWAYS);

        registerBtn = new Button("Register");
        registerBtn.setStyle(ACTIVE_TOGGLE_STYLE);
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(registerBtn, Priority.ALWAYS);

        toggleBox.getChildren().addAll(loginBtn, registerBtn);

        Label roleLabel = new Label("I am a");
        roleLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        roleLabel.setTextFill(Color.web("#344563"));

        HBox roleBox = new HBox(15);
        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton patientBtn = createRoleButton("Patient", true, roleGroup);
        ToggleButton doctorBtn = createRoleButton("Doctor", false, roleGroup);
        ToggleButton hospitalBtn = createRoleButton("Hospital", false, roleGroup);
        roleBox.getChildren().addAll(patientBtn, doctorBtn, hospitalBtn);

        dynamicFieldsContainer = new VBox(15);

        actionBtn = new Button("Create account");
        actionBtn.setStyle("-fx-background-color: #1d4b8f; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-cursor: hand;");
        actionBtn.setPrefHeight(45);
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(actionBtn, new Insets(10, 0, 5, 0));

        updateFormFields();

        // Switcher logic hooks
        loginBtn.setOnAction(e -> {
            isRegisterMode = false;
            welcome.setText("Welcome back");
            loginBtn.setStyle(ACTIVE_TOGGLE_STYLE);
            registerBtn.setStyle(INACTIVE_TOGGLE_STYLE);
            actionBtn.setText("Sign in");
            updateFormFields();
        });

        registerBtn.setOnAction(e -> {
            isRegisterMode = true;
            welcome.setText("Welcome");
            registerBtn.setStyle(ACTIVE_TOGGLE_STYLE);
            loginBtn.setStyle(INACTIVE_TOGGLE_STYLE);
            actionBtn.setText("Create account");
            updateFormFields();
        });

        patientBtn.setOnAction(e -> { currentRole = "Patient"; updateFormFields(); });
        doctorBtn.setOnAction(e -> { currentRole = "Doctor"; updateFormFields(); });
        hospitalBtn.setOnAction(e -> { currentRole = "Hospital"; updateFormFields(); });

        actionBtn.setOnAction(e -> {
            if (isRegisterMode) {
                if (currentRole.equals("Patient")) registerPatientToDatabase();
                else if (currentRole.equals("Doctor")) registerDoctorToDatabase();
                else if (currentRole.equals("Hospital")) registerHospitalToDatabase();
            } else {
                if (currentRole.equals("Patient")) validatePatientLogin();
                else if (currentRole.equals("Doctor")) validateDoctorLogin();
                else if (currentRole.equals("Hospital")) validateHospitalLogin();
            }
        });

        Label footer = new Label("By E-Healthcare Management Group");
        footer.setTextFill(Color.web("#a5b2c6"));
        footer.setFont(Font.font("System", 11));
        footer.setMaxWidth(Double.MAX_VALUE);
        footer.setAlignment(Pos.CENTER);

        formBox.getChildren().addAll(welcome, signinSub, toggleBox, roleLabel, roleBox, dynamicFieldsContainer, actionBtn, footer);
        rightPaneContainer.getChildren().add(formBox);
        root.getChildren().addAll(leftPane, rightPaneContainer);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Registration Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean containsUppercase(String email) {
        if (email == null) return false;
        for (char c : email.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStrictlyValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        if (email.length() > 320) return false;

        int atCount = 0;
        for (char c : email.toCharArray()) {
            if (c == '@') atCount++;
        }
        if (atCount != 1) return false;

        String[] parts = email.split("@");
        if (parts.length != 2) return false;

        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() < 1 || localPart.length() > 64) return false;
        if (domain.length() < 3 || domain.length() > 255) return false;
        if (localPart.startsWith(".") || localPart.endsWith(".") || localPart.contains("..")) return false;

        if (!localPart.matches("^[a-z0-9!#$%&'*+\\-/=?^_`{|}~.]+$")) return false;
        if (domain.startsWith(".") || domain.endsWith(".") || domain.contains("..")) return false;

        String[] labels = domain.split("\\.");
        if (labels.length < 2) return false;

        for (int i = 0; i < labels.length - 1; i++) {
            String label = labels[i];
            if (label.length() < 1 || label.length() > 63) return false;
            if (label.startsWith("-") || label.endsWith("-")) return false;
            if (!label.matches("^[a-z0-9-]+$")) return false;
        }

        String tld = labels[labels.length - 1];
        if (tld.length() < 2 || tld.length() > 63) return false;
        if (!tld.matches("^[a-z]+$")) return false;

        return true;
    }

    private boolean isStrictlyValidNigerianNumber(String phone) {
        if (phone == null) return false;
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        if (cleanPhone.startsWith("0") && cleanPhone.length() == 11) {
            cleanPhone = "+234" + cleanPhone.substring(1);
        } else if (cleanPhone.startsWith("234") && cleanPhone.length() == 13) {
            cleanPhone = "+" + cleanPhone;
        }
        if (!cleanPhone.matches("^\\+234\\d{10}$")) return false;
        String operatorPrefix = cleanPhone.substring(4, 7);
        return VALID_NIGERIAN_PREFIXES.contains(operatorPrefix);
    }

    private String sanitizeToNigerianDatabaseFormat(String phone) {
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");
        if (cleanPhone.startsWith("0")) return "+234" + cleanPhone.substring(1);
        else if (cleanPhone.startsWith("234")) return "+" + cleanPhone;
        return cleanPhone;
    }

    private boolean isValidPatientDateOfBirth(String dobString) {
        if (dobString == null || !dobString.matches("^\\d{4}-\\d{2}-\\d{2}$")) return false;
        try {
            LocalDate dob = LocalDate.parse(dobString);
            LocalDate today = LocalDate.now();
            return (!dob.isAfter(today)) && (!dob.isBefore(today.minusYears(150)));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private String determineCategoryFromLicense(String license) {
        if (license == null) return null;
        String input = license.trim().toUpperCase();
        if (!input.startsWith("MDCN/")) return null;
        if (!input.matches("^MDCN/([A-Z]{1,3}/?)?\\d{5,7}$")) return null;

        String remaining = input.substring(5);
        String prefixCode = remaining.contains("/") ? remaining.split("/")[0] : remaining.replaceAll("\\d", "");

        switch (prefixCode) {
            case "D": case "DT": case "DEN": case "PD": case "FMD": return "Dentist";
            case "C": case "CON": case "CNS": return "Consultant";
            case "R": case "RES": case "REG": return "Resident";
            case "PM": return "Provisional Medical";
            case "T": case "TEMP": return "Temporary Medical Practitioner";
            case "M": case "MED": case "FMM": return "Medical Practitioner";
            case "": return "General Practitioner";
            default: return null;
        }
    }

    private void updateFormFields() {
        dynamicFieldsContainer.getChildren().clear();
        if (!isRegisterMode) {
            loginEmailField = new TextField();
            VBox emailBox = createLabeledField("Email", loginEmailField);
            loginPasswordField = new PasswordField();
            VBox passwordBox = createLabeledField("Password", loginPasswordField);
            dynamicFieldsContainer.getChildren().addAll(emailBox, passwordBox);
        } else {
            switch (currentRole) {
                case "Patient":
                    pFullNameField = new TextField();
                    VBox pFullNameBox = createLabeledField("Full name", pFullNameField);
                    HBox dobContactBox = new HBox(15);
                    pDobField = new TextField(); pDobField.setPromptText("YYYY-MM-DD");
                    VBox dobBox = createLabeledField("Date of birth", pDobField); dobBox.setPrefWidth(120);
                    pContactField = new TextField(); pContactField.setPromptText("+234 803 123 4567");
                    VBox contactBox = createLabeledField("Contact Number", pContactField);
                    HBox.setHgrow(contactBox, Priority.ALWAYS);
                    dobContactBox.getChildren().addAll(dobBox, contactBox);
                    pEmailField = new TextField();
                    VBox pEmailBox = createLabeledField("Email", pEmailField);
                    pPasswordField = new PasswordField();
                    VBox pPasswordBox = createLabeledField("Password", pPasswordField);
                    dynamicFieldsContainer.getChildren().addAll(pFullNameBox, dobContactBox, pEmailBox, pPasswordBox);
                    break;

                case "Doctor":
                    dFullNameField = new TextField();
                    VBox dFullNameBox = createLabeledField("Full name", dFullNameField);
                    HBox licenseSpecialtyBox = new HBox(15);
                    dLicenseField = new TextField(); dLicenseField.setPromptText("e.g. mdcn/pm/12345");
                    VBox licenseBox = createLabeledField("License number", dLicenseField); HBox.setHgrow(licenseBox, Priority.ALWAYS);
                    dSpecialtyField = new TextField();
                    VBox specialtyBox = createLabeledField("Specialty", dSpecialtyField); HBox.setHgrow(specialtyBox, Priority.ALWAYS);
                    licenseSpecialtyBox.getChildren().addAll(licenseBox, specialtyBox);
                    dCategoryTypeField = new TextField(); dCategoryTypeField.setEditable(false);
                    dCategoryTypeField.setPromptText("Auto-populated from License");
                    VBox categoryTypeBox = createLabeledField("Category", dCategoryTypeField);

                    dLicenseField.textProperty().addListener((observable, oldValue, newValue) -> {
                        String matchedCat = determineCategoryFromLicense(newValue);
                        if (matchedCat != null) {
                            dCategoryTypeField.setText(matchedCat);
                            if (matchedCat.equals("Dentist")) {
                                dSpecialtyField.setText("Dentist");
                            } else {
                                if ("Dentist".equals(dSpecialtyField.getText())) {
                                    dSpecialtyField.clear();
                                }
                            }
                        } else {
                            dCategoryTypeField.setText("");
                            if ("Dentist".equals(dSpecialtyField.getText())) {
                                dSpecialtyField.clear();
                            }
                        }
                    });

                    dEmailField = new TextField();
                    VBox dEmailBox = createLabeledField("Email", dEmailField);
                    dPasswordField = new PasswordField();
                    VBox dPasswordBox = createLabeledField("Password", dPasswordField);
                    dynamicFieldsContainer.getChildren().addAll(dFullNameBox, licenseSpecialtyBox, categoryTypeBox, dEmailBox, dPasswordBox);
                    break;

                case "Hospital":
                    hNameField = new TextField();
                    VBox hospitalNameBox = createLabeledField("Hospital name", hNameField);
                    HBox branchAdminBox = new HBox(15);
                    hBranchDropdown = new ComboBox<>();
                    hBranchDropdown.getItems().addAll("Agege", "Alimosho", "Ikeja", "Ikorodu", "Lagos Island", "Surulere");
                    hBranchDropdown.setValue("Agege"); hBranchDropdown.setMaxWidth(Double.MAX_VALUE);
                    VBox branchBox = createLabeledField("Branch / Location", hBranchDropdown); HBox.setHgrow(branchBox, Priority.ALWAYS);
                    hAdminIdField = new TextField();
                    VBox adminIdBox = createLabeledField("Admin ID", hAdminIdField); adminIdBox.setPrefWidth(150);
                    branchAdminBox.getChildren().addAll(branchBox, adminIdBox);
                    hEmailField = new TextField();
                    VBox hEmailBox = createLabeledField("Email", hEmailField);
                    hPasswordField = new PasswordField();
                    VBox hPasswordBox = createLabeledField("Password", hPasswordField);
                    dynamicFieldsContainer.getChildren().addAll(hospitalNameBox, branchAdminBox, hEmailBox, hPasswordBox);
                    break;
            }
        }
    }

    private void validatePatientLogin() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please fill in all fields before submitting.");
            return;
        }
        if (containsUppercase(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Form Entry", "Email addresses must be inputted strictly using lowercase characters.");
            return;
        }

        String sqlQuery = "SELECT password, full_name FROM patients WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getString("password").equals(password)) {
                        showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome back, " + resultSet.getString("full_name") + "!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Login Failure", "Incorrect password. Please try again.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Account Not Found", "No account registered under this email.");
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
        }
    }

    private void validateDoctorLogin() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please fill in all fields before submitting.");
            return;
        }
        if (containsUppercase(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Form Entry", "Email addresses must be inputted strictly using lowercase characters.");
            return;
        }

        String sqlQuery = "SELECT password, full_name FROM doctors WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getString("password").equals(password)) {
                        showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome back, Dr. " + resultSet.getString("full_name") + "!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Login Failure", "Incorrect password. Please try again.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Account Not Found", "No account registered under this email.");
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
        }
    }

    private void validateHospitalLogin() {
        String email = loginEmailField.getText().trim();
        String password = loginPasswordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Please fill in all fields before submitting.");
            return;
        }
        if (containsUppercase(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Form Entry", "Email addresses must be inputted strictly using lowercase characters.");
            return;
        }

        String sqlQuery = "SELECT password, hospital_name FROM hospital WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getString("password").equals(password)) {
                        showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome back, Admin for " + resultSet.getString("hospital_name") + "!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Login Failure", "Incorrect password. Please try again.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Account Not Found", "No account registered under this email.");
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
        }
    }

    private void registerPatientToDatabase() {
        String fullName = pFullNameField.getText().trim();
        String dob = pDobField.getText().trim();
        String contact = pContactField.getText().trim();
        String email = pEmailField.getText().trim();
        String password = pPasswordField.getText().trim();

        if (fullName.isEmpty() || dob.isEmpty() || contact.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all fields before submitting.");
            return;
        }
        if (containsUppercase(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Form Entry", "Email addresses must be inputted strictly using lowercase characters.");
            return;
        }
        if (!isValidPatientDateOfBirth(dob)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date of Birth", "Please enter a valid format (YYYY-MM-DD).");
            return;
        }
        if (!isStrictlyValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email Structure", "The email configuration does not conform to registration guidelines.");
            return;
        }
        if (!isStrictlyValidNigerianNumber(contact)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Nigerian Contact Number", "Please provide a valid Nigerian mobile phone number.");
            return;
        }

        String checkQuery = "SELECT email FROM patients WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, email);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    showAlert(Alert.AlertType.WARNING, "Account Already Exists", "An account with this email is already registered. Taking you to the Patient Login page.");
                    loginBtn.fire();
                    if (loginEmailField != null) loginEmailField.setText(email);
                    return;
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
            return;
        }

        String sqlQuery = "INSERT INTO patients (full_name, dob, contact, email, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, dob);
            preparedStatement.setString(3, sanitizeToNigerianDatabaseFormat(contact));
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully for " + fullName + "!");
                clearFormFields();
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
        }
    }

    private void registerDoctorToDatabase() {
        String fullName = dFullNameField.getText().trim();
        String licenseInput = dLicenseField.getText().trim();
        String specialty = dSpecialtyField.getText().trim();
        String category = dCategoryTypeField.getText().trim();
        String email = dEmailField.getText().trim();
        String password = dPasswordField.getText().trim();

        String validatedCategory = determineCategoryFromLicense(licenseInput);
        if (validatedCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid License Number", "License format unrecognized. Must match standard MDCN prefix formats followed by 5 to 7 digits.");
            return;
        }
        if (containsUppercase(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Form Entry", "Email addresses must be inputted strictly using lowercase characters.");
            return;
        }
        if (validatedCategory.equals("Dentist")) {
            specialty = "Dentist";
            category = "Dentist";
        }
        if (fullName.isEmpty() || licenseInput.isEmpty() || specialty.isEmpty() || category.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all doctor fields.");
            return;
        }
        if (!isStrictlyValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email Structure", "The email configuration does not conform to registration guidelines.");
            return;
        }

        String checkQuery = "SELECT email FROM doctors WHERE email = ? OR license_number = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, email);
            checkStmt.setString(2, licenseInput.toUpperCase());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    showAlert(Alert.AlertType.WARNING, "Account Already Exists", "A doctor with this email or license is already registered. Taking you to the Doctor Login page.");
                    loginBtn.fire();
                    if (loginEmailField != null) loginEmailField.setText(email);
                    return;
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
            return;
        }

        String sqlQuery = "INSERT INTO doctors (full_name, license_number, specialty, category, email, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, licenseInput.toUpperCase());
            preparedStatement.setString(3, specialty);
            preparedStatement.setString(4, category);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, password);

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Doctor account created successfully!");
                clearDoctorFormFields();
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
        }
    }

    private void registerHospitalToDatabase() {
        String hospitalName = hNameField.getText().trim();
        String branchLocation = hBranchDropdown.getValue();
        String adminId = hAdminIdField.getText().trim();
        String email = hEmailField.getText().trim();
        String password = hPasswordField.getText().trim();

        if (hospitalName.isEmpty() || branchLocation == null || adminId.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all hospital fields.");
            return;
        }
        if (containsUppercase(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Form Entry", "Email addresses must be inputted strictly using lowercase characters.");
            return;
        }
        if (!isStrictlyValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email Structure", "The email configuration does not conform to registration guidelines.");
            return;
        }

        String checkQuery = "SELECT email FROM hospital WHERE email = ? OR admin_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, email);
            checkStmt.setString(2, adminId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    showAlert(Alert.AlertType.WARNING, "Account Already Exists", "A hospital branch matching this email or admin ID configuration already exists. Taking you to Login.");
                    loginBtn.fire();
                    if (loginEmailField != null) loginEmailField.setText(email);
                    return;
                }
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
            return;
        }

        String sqlQuery = "INSERT INTO hospital (hospital_name, branch_location, admin_id, email, password, free_beds, occupied_beds, total_beds) VALUES (?, ?, ?, ?, ?, 0, 0, 0)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, hospitalName);
            preparedStatement.setString(2, branchLocation);
            preparedStatement.setString(3, adminId);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, password);

            if (preparedStatement.executeUpdate() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Hospital account created successfully!");
                clearHospitalFormFields();
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Failure", ex.getMessage());
        }
    }

    private void clearFormFields() {
        if (pFullNameField != null) pFullNameField.clear();
        if (pDobField != null) pDobField.clear();
        if (pContactField != null) pContactField.clear();
        if (pEmailField != null) pEmailField.clear();
        if (pPasswordField != null) pPasswordField.clear();
    }

    private void clearDoctorFormFields() {
        if (dFullNameField != null) dFullNameField.clear();
        if (dLicenseField != null) dLicenseField.clear();
        if (dSpecialtyField != null) dSpecialtyField.clear();
        if (dCategoryTypeField != null) dCategoryTypeField.clear();
        if (dEmailField != null) dEmailField.clear();
        if (dPasswordField != null) dPasswordField.clear();
    }

    private void clearHospitalFormFields() {
        if (hNameField != null) hNameField.clear();
        if (hAdminIdField != null) hAdminIdField.clear();
        if (hEmailField != null) hEmailField.clear();
        if (hPasswordField != null) hPasswordField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private HBox createBulletPoint(String text) {
        HBox hbox = new HBox(15);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label check = new Label("v");
        check.setStyle("-fx-background-color: #3b4e6d; -fx-text-fill: white; -fx-background-radius: 50; -fx-padding: 2 6 2 6;");
        check.setFont(Font.font("System", FontWeight.BOLD, 12));
        Label content = new Label(text);
        content.setTextFill(Color.WHITE);
        content.setFont(Font.font("System", 14));
        hbox.getChildren().addAll(check, content);
        return hbox;
    }

    private ToggleButton createRoleButton(String text, boolean isSelected, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(isSelected);
        btn.setPrefSize(140, 70);

        String baseStyle = "-fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand; ";
        String selectedStyle = baseStyle + "-fx-background-color: #f1f2fa; -fx-border-color: #0052cc; -fx-border-width: 1.5;";
        String unselectedStyle = baseStyle + "-fx-background-color: white; -fx-border-color: #dfe1e6; -fx-border-width: 1;";

        btn.setStyle(isSelected ? selectedStyle : unselectedStyle);
        btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            btn.setStyle(newVal ? selectedStyle : unselectedStyle);
        });
        return btn;
    }

    private VBox createLabeledField(String labelText, Control field) {
        VBox box = new VBox(5);
        Label label = new Label(labelText);
        label.setFont(Font.font("System", FontWeight.BOLD, 12));
        label.setTextFill(Color.web("#344563"));
        field.setStyle("-fx-background-color: white; -fx-border-color: #dfe1e6; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 10;");
        box.getChildren().addAll(label, field);
        return box;
    }
}
