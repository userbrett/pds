/**
 * ShowHeap.java
 *
 * Personal Data Security (PDS)
 * Copyright 2016, PDS Software Solutions LLC - www.trustpds.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Brett Lee <crypto411@gmail.com>
 * @see <a href="https://www.trustpds.com/">www.trustpds.com</a>
 */

/*
 * This class prints out memory heap values.  It may be useful
 * when running sustained encryption operations.
 * Inspired by: http://stackoverflow.com/questions/2015463/
 */

public class ShowHeap {

    public static void main(String[] args) {

    	// Get current size of heap in bytes
    	long heapSize = Runtime.getRuntime().totalMemory(); 

    	// Get maximum size of heap in bytes. The heap cannot grow beyond
    	// this size.  Any attempt will result in an OutOfMemoryException.
    	long heapMaxSize = Runtime.getRuntime().maxMemory();

    	// Get amount of free memory within the heap in bytes.
    	// This size will increase after garbage collection and
    	// decrease as new objects are created.
    	long heapFreeSize = Runtime.getRuntime().freeMemory(); 

    	System.out.println("Current Heap Size (MB): " + (heapSize/(1024*1024)) );
    	System.out.println("Maximum Heap Size (MB): " + (heapMaxSize/(1024*1024)) );
    	System.out.println("Free Heap Memory  (MB): " + (heapFreeSize/(1024*1024)) );
    }
}
