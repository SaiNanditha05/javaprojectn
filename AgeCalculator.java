public class AgeCalculator {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java AgeCalculator <DOB/AGE> <reference_date> <date_format> <delimiter>");
            return;
        }

        String inputParam = args[0];  // DOB or AGE
        String referenceDateStr = args[1];  // Reference date
        String dateFormat = args[2];  // Date format (DD-MM-YYYY, MM-DD-YYYY, YYYY-MM-DD)
        String delimiter = args[3];  // Delimiter

        // Parse and validate the reference date
        int[] referenceDate = parseDate(referenceDateStr, dateFormat, delimiter);
        if (referenceDate == null) {
            System.out.println("Error: Invalid reference date.");
            return;
        }

        if (inputParam.startsWith("DOB")) {
            // Calculate age from DOB
            String dobStr = inputParam.split("=")[1];
            int[] dob = parseDate(dobStr, dateFormat, delimiter);
            if (dob == null) {
                System.out.println("Error: Invalid DOB.");
                return;
            }

            // Calculate age
            calculateAge(dob, referenceDate);
        } else if (inputParam.startsWith("AGE")) {
            // Calculate DOB from age
            String ageStr = inputParam.split("=")[1];
            String[] ageParts = ageStr.split(delimiter);

            if (ageParts.length != 3) {
                System.out.println("Error: Invalid age format.");
                return;
            }

            try {
                int years = Integer.parseInt(ageParts[0]);
                int months = Integer.parseInt(ageParts[1]);
                int days = Integer.parseInt(ageParts[2]);

                int[] dob = calculateDob(years, months, days, referenceDate);
                System.out.println("DOB is " + formatDate(dob));
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid age format.");
            }
        } else {
            System.out.println("Error: Invalid input. Please use 'DOB' or 'AGE'.");
        }
    }

    // Method to parse a date from a string based on the date format and delimiter
    private static int[] parseDate(String dateStr, String dateFormat, String delimiter) {
        try {
            // Replace the delimiter with '-'
            String normalizedDate = dateStr.replace(delimiter, "-");

            String[] dateParts = null;
            switch (dateFormat) {
                case "DD-MM-YYYY":
                    dateParts = normalizedDate.split("-");
                    if (dateParts.length != 3) return null;
                    return new int[] { Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]) };

                case "MM-DD-YYYY":
                    dateParts = normalizedDate.split("-");
                    if (dateParts.length != 3) return null;
                    return new int[] { Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[2]) };

                case "YYYY-MM-DD":
                    dateParts = normalizedDate.split("-");
                    if (dateParts.length != 3) return null;
                    return new int[] { Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]) };

                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    // Method to format a date to a string (DD-MM-YYYY format)
    private static String formatDate(int[] date) {
        return String.format("%02d-%02d-%04d", date[0], date[1], date[2]);
    }

    // Method to calculate age from DOB
    private static void calculateAge(int[] dob, int[] referenceDate) {
        int years = referenceDate[2] - dob[2];
        int months = referenceDate[1] - dob[1];
        int days = referenceDate[0] - dob[0];

        // Adjust for negative months or days
        if (days < 0) {
            days += getDaysInMonth(referenceDate[1] - 1, referenceDate[2]);
            months--;
        }

        if (months < 0) {
            months += 12;
            years--;
        }

        System.out.println("Age is " + years + " years, " + months + " months, " + days + " days");
    }

    // Method to calculate DOB from age
    private static int[] calculateDob(int years, int months, int days, int[] referenceDate) {
        int[] dob = new int[3];

        dob[2] = referenceDate[2] - years;
        dob[1] = referenceDate[1] - months;
        dob[0] = referenceDate[0] - days;

        // Adjust for negative months or days
        if (dob[0] < 1) {
            dob[0] += getDaysInMonth(referenceDate[1] - 1, referenceDate[2]);
            dob[1]--;
        }

        if (dob[1] < 1) {
            dob[1] += 12;
            dob[2]--;
        }

        return dob;
    }

    // Helper method to get the number of days in a given month (taking into account leap years)
    private static int getDaysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return (isLeapYear(year)) ? 29 : 28;
            default:
                return 0;
        }
    }

    // Helper method to check if a year is a leap year
    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    }
}