package org.rcsb.mmtf.decoder;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;


public class TestArrayConverters {


	/**
	 * Test the decoding of the chain list
	 */
	@Test 
	public final void testConvertChainList() {
		byte[] byteArray = {'A','\0','\0','\0',
				'A','B','C','\0',
				'a','b','c','\0'};
		String[] testStringList = {"A","ABC","abc"};
		String[] stringList = ArrayConverters.decodeChainList(byteArray);
		assertArrayEquals(testStringList, stringList);
	}
	
	
	/**
	 * Test the conversion of the integer array to a float 
	 */
	@Test 
	public final void testConvertIntToFloat() {
		int[] intArray = {10001,100203,124542};
		float[] testFloatArray = {10.001f,100.203f,124.542f};
		float divider = 1000.0f;
		float[] floatArray = ArrayConverters.convertIntsToFloats(intArray,divider);
		assertArrayEquals(testFloatArray, floatArray, 0.0f);
	}
	
	
	
	/**
	 * Test the conversion of byte arrays to one byte integer arrays
	 * @throws IOException 
	 */
	@Test
	public final void oneByteToIntegersTest() throws IOException {
		int[] testIntArray = {12,123,24};
		byte[] byteArray = {(byte) 12, (byte) 123, (byte) 24};
		int[] intArray = ArrayConverters.convertByteToIntegers(byteArray);
		assertArrayEquals(testIntArray, intArray);
	}

	/**
	 * Test the conversion of byte arrays to two byte integer arrays
	 * @throws IOException 
	 */
	@Test
	public final void twoByteToIntegersTest() throws IOException {
		int[] testIntArray = {1000,1002,546};
		byte[] byteArray = getByteArray(testIntArray,2);
		int[] intArray = ArrayConverters.convertTwoByteToIntegers(byteArray);
		assertArrayEquals(testIntArray, intArray);
	}

	/**
	 * Test the conversion of byte arrays to four byte integer arrays
	 * @throws IOException 
	 */
	@Test
	public final void fourByteToIntegersTest() throws IOException {	
		int[] testIntArray = {32403,11200,100090};
		byte[] byteArray = getByteArray(testIntArray,4);
		int[] intArray = ArrayConverters.convertFourByteToIntegers(byteArray);
		assertArrayEquals(testIntArray, intArray);
	}

	/**
	 * Utiliy function to get a byte array. I don't really like this but at least
	 * it's an orthogonal approach.
	 * @param inArray the input int array
	 * @param numBytes the number of bytes per integer
	 * @return the output byte array
	 */
	private byte[] getByteArray(int[] inArray, int numBytes) {
		byte[] outBytes = new byte[inArray.length*numBytes];
		for(int i=0; i<inArray.length;i++){
			byte[] intBytes;
			if (numBytes==4){
				intBytes = ByteBuffer.allocate(numBytes).putInt(inArray[i]).array();
			}
			else if (numBytes==2){
				intBytes = ByteBuffer.allocate(numBytes).putShort((short) inArray[i]).array();
			}
			else if (numBytes==1){
				intBytes = ByteBuffer.allocate(numBytes).put((byte) inArray[i]).array();
			}
			else{
				intBytes = new byte[numBytes];
			}
			for(int j=0; j<numBytes; j++){
				outBytes[i*numBytes+j] = intBytes[j];
			}
		}
		return outBytes;
	}

	/**
	 * Test the conversion of integer arrays to char arrays.
	 */
	@Test
	public final void combineArraysTest() {
		// Initialise the two input arrays
		int[] twoByteIntArray = {1,2,5,4,50,0};
		int[] fourByteIntArray = {10002,4,1002,2};
		// The expected output
		int[] testCombinedArray = {10002,1,2,5,4,1002,50,0};
		int[] combinedArray = ArrayConverters.combineIntegers(twoByteIntArray, fourByteIntArray);
		assertArrayEquals(testCombinedArray, combinedArray);
	}


	/**
	 * Test the conversion of integer arrays to char arrays.
	 */
	@Test
	public final void intToCharTest() {
		int[] inputData =  {66,63,67};
		char[] outPutDataTest = {'B','?','C'};
		char[] outPutData = ArrayConverters.convertIntegerToChar(inputData);
		assertArrayEquals(outPutDataTest, outPutData);
	}

}
