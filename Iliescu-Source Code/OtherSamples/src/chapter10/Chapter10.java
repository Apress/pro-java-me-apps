package chapter10;

public class Chapter10 {

    public static void quicksort(int [] inputSet, int start, int end)
    {
        int left = start;
        int right = end;
        int temp = 0;

        // First, we find the pivot value
        int pivotValue = inputSet[ (start+end)/2];

        // Then, we arrange the input set into { smaller than set, pivot, greater than set }.
        // The process for this is as follows:
        // 0: Initial left position = start; initial right position = end.
        // 1: Starting from the current left positon, find a value greater than the pivot. We will call this value A.
        // 2: Starting from the current right position, find a value smaller than the pivot. We will call this value B.
        // 3: Swap A and B. Increase left position and decrease right position.
        // 4: Repeat from step 1 until the left position index is equal to or greater than the right position index.

        // Next, while the left index is smaller than or equal to the right index, ....
        while (left<=right)
        {
            // ... we find a suitable A ...
            while ( inputSet[left] < pivotValue )
            {
                left++;
            }

            // ... then we find a suitable B ...
            while ( inputSet[right] > pivotValue )
            {
                right--;
            }

            // .. then, provided left <= right, ...
            if ( left <= right )
            {
                // ... we swap A and B.
                temp = inputSet[left];
                inputSet[left] = inputSet[right];
                inputSet[right] = temp;

                // Also, don't forget to increase left ...
                left++;

                // ... and decrease right. We do this so that we do not swap the same two values forever.
                right--;
            }
        }

        // After the input set has been arranged. we apply Quicksort on the "smaller than" subset,
        // if the set is at least two elements big.
        if ( start < right )
        {
            quicksort(inputSet,start,right);
        }

        // We then apply Quicksort on the "greater than" subset, again if the set is big enough.
        if ( end > left )
        {
            quicksort(inputSet,left,end);
        }
    }

    public static int binarySearch(int [] inputSet, int element)
    {
        int start = 0;
        int end = inputSet.length-1;
        int middleValue = 0;
        int middleIndex = 0;

        // As long as the interval has at least one element
        while ( start<=end )
        {
            // Get the index of the middle element
            middleIndex = (start+end) / 2;

            // Get the value of the middle element of the interval
            middleValue = inputSet[middleIndex];

            // And compare it to the target criteria/element
            if ( middleValue == element )
            {
                // If it matches, we have our result
                return middleIndex;
            }
            else if ( middleValue > element )
            {
                // If it is greater, use the interval (middle,end]
                end = middleIndex-1;
            }
            else
            {
                // If it is smaller, use the interval [start,middle)
                start = middleIndex+1;
            }
        }

        // Interval has zero elements, no match found
        return -1;
    }

    public static int fibBuffer[] = new int [1000];

    public static int fib(int n)
    {
        if ( n < 2 )
        {
            return n;
        }
        else
        {
            return fib(n-1) + fib(n-2);
        }
    }

    public static int fibBottomUp(int n)
    {
        fibBuffer[0] = 0;
        fibBuffer[1] = 1;
        int index=2;
        while (index<=n)
        {
            fibBuffer[index] = fibBuffer[index-2] + fibBuffer[index-1];
            index++;
        }
        return fibBuffer[n];
    }

    public static int fibTopDown(int n)
    {
        if ( n < 2 )
        {
            return n;
        }
        else if ( fibBuffer[n] != 0 )
        {
            return fibBuffer[n];
        }
        else
        {
            fibBuffer[n] = fibTopDown(n-1) + fibTopDown(n-2);
            return fibBuffer[n];
        }
    }

}
