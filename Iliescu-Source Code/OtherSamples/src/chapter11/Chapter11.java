package chapter11;

public class Chapter11 {

    public static int editDistance(String s1, String s2) {

        int i,j;
        int sizeS1 = s1.length();
        int sizeS2 = s2.length();

        // Create the edit distance matrix
        int distances[][] = new int[sizeS1+1][sizeS2+1];

        // Initialize the matrix
        for (i=0; i<=sizeS1; i++) {
            distances[i][0] = i;
        }
        for (j=0; j<=sizeS2; j++) {
            distances[0][j] = j;
        }

        // Calculate the edit distance
        for (i=1; i<=sizeS1; i++) {
            for (j=1; j<=sizeS2; j++) {

                if ( s1.charAt(i-1) == s2.charAt(j-1) ) {
                    // No operation required, cost stays the same
                    distances[i][j] = distances[i-1][j-1];
                }
                else
                {
                    // Calculate the minimum distance based on the three operations
                    // we can do at this point:
                    distances[i][j] = Math.min (
                            // Delete S1[i]
                            distances[i-1][j] + 1,
                            // Insert S2[j]
                            Math.min ( distances[i][j-1] + 1,
                            // Substitute S1[i] for S2[j]
                            distances[i-1][j-1] + 1 ) );
                }
            }
        }

        return distances[sizeS1][sizeS2];
    }

}
