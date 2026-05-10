# QUICK START GUIDE

## For Windows Users

### Step 1: Compile the Project
Double-click `compile.bat` or run in Command Prompt:
```
compile.bat
```

### Step 2: Run the Application
Double-click `run.bat` or run in Command Prompt:
```
run.bat
```

## For Linux/macOS Users

### Step 1: Make Scripts Executable
Open Terminal and run:
```bash
chmod +x compile.sh run.sh
```

### Step 2: Compile the Project
```bash
./compile.sh
```

### Step 3: Run the Application
```bash
./run.sh
```

## Or Compile & Run Manually (All Platforms)

```bash
# Navigate to project directory
cd path/to/TMRCS

# Compile
javac -d bin src/com/telemedicine/*.java src/com/telemedicine/models/*.java src/com/telemedicine/utils/*.java

# Run
java -cp bin com.telemedicine.Main
```

## First Time Testing

1. When the app starts, sample data is loaded (3 doctors, 1 patient, 1 admin)
2. Try logging in with the default credentials:
   - **Patient**: ahmed@email.com / pass123
   - **Doctor**: fatima@hospital.com / doc123
   - **Admin**: admin@system.com / admin123

3. Or register a new patient account

## What to Test

1. **Patient Registration** → Login → View Profile → Search Doctors
2. **Doctor Availability** → Set time slots → View appointments
3. **Book Appointment** → Select doctor → Choose time → Confirm
4. **Prescription** → Login as doctor → Issue prescription
5. **Exit & Restart** → Data should persist

## Troubleshooting

- **"Main class not found"** → Run compile.bat/sh first
- **"No doctors available"** → Sample doctors load automatically on first run
- **Date format issues** → Use DD-MM-YYYY format
- **Time format issues** → Use 24-hour format (HH:MM)

## Project Structure

```
TMRCS/
├── src/                    (Source code)
├── bin/                    (Compiled classes - created after compile)
├── data/                   (Data files - created automatically)
├── compile.bat             (Windows compile script)
├── compile.sh              (Linux/macOS compile script)
├── run.bat                 (Windows run script)
├── run.sh                  (Linux/macOS run script)
├── README.md               (Full documentation)
└── QUICK_START.md          (This file)
```

## System Requirements

- Java JDK 11 or higher
- 50MB disk space
- Terminal/Command Prompt access

Happy testing!
