package org.rcsb.mmtf.decoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.rcsb.mmtf.api.DataApiInterface;
import org.rcsb.mmtf.arraydecompressors.DeltaDeCompress;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeInt;
import org.rcsb.mmtf.arraydecompressors.RunLengthDecodeString;
import org.rcsb.mmtf.arraydecompressors.RunLengthDelta;
import org.rcsb.mmtf.dataholders.BioAssemblyData;
import org.rcsb.mmtf.dataholders.Entity;
import org.rcsb.mmtf.dataholders.MmtfBean;
import org.rcsb.mmtf.dataholders.PDBGroup;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleDataApi implements DataApiInterface {
	
	
	public SimpleDataApi(byte[] inputByteArr) {
		
		
		MmtfBean inputData = null;
		try {
			inputData = new ObjectMapper(new MessagePackFactory()).readValue(inputByteArr, MmtfBean.class);
		} catch (IOException e) {
			// 
			System.out.println("Error converting Byte array to message pack. IOError");
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		// Get the decompressors to build in the data structure
		DeltaDeCompress deltaDecompress = new DeltaDeCompress();
		RunLengthDelta intRunLengthDelta = new RunLengthDelta();
		RunLengthDecodeInt intRunLength = new RunLengthDecodeInt();
		RunLengthDecodeString stringRunlength = new RunLengthDecodeString();
		DecoderUtils decoderUtils = new DecoderUtils();
		
		// Get the data
		try {
			groupList = decoderUtils.bytesToInts(inputData.getGroupTypeList());
			// Read the byte arrays as int arrays
			cartnX = decoderUtils.decodeIntsToFloats(deltaDecompress.decompressByteArray(inputData.getxCoordBig(), inputData.getxCoordSmall()), MmtfBean.COORD_DIVIDER);
			cartnY = decoderUtils.decodeIntsToFloats(deltaDecompress.decompressByteArray(inputData.getyCoordBig(), inputData.getyCoordSmall()), MmtfBean.COORD_DIVIDER);
			cartnZ = decoderUtils.decodeIntsToFloats(deltaDecompress.decompressByteArray(inputData.getzCoordBig(), inputData.getzCoordSmall()), MmtfBean.COORD_DIVIDER);
			bFactor =  decoderUtils.decodeIntsToFloats(deltaDecompress.decompressByteArray(inputData.getbFactorBig(),inputData.getbFactorSmall()), MmtfBean.OCCUPANCY_BFACTOR_DIVIDER);
			occupancy = decoderUtils.decodeIntsToFloats(intRunLength.decompressByteArray(inputData.getOccList()), MmtfBean.OCCUPANCY_BFACTOR_DIVIDER);
			atomId = intRunLengthDelta.decompressByteArray(inputData.getAtomIdList());
			altId = stringRunlength.stringArrayToChar(
					(ArrayList<String>) inputData.getAltLabelList());
			// Get the insertion code
			insertionCodeList = stringRunlength.stringArrayToChar(
					(ArrayList<String>) inputData.getInsCodeList());
			// Get the groupNumber
			groupNum = intRunLengthDelta.decompressByteArray(
					inputData.getGroupIdList());
			groupMap = inputData.getGroupMap();
			// Get the seqRes groups
			seqResGroupList = intRunLengthDelta.decompressByteArray(inputData.getSeqResIdList());
			// Get the number of chains per model
			chainsPerModel = inputData.getChainsPerModel();
			groupsPerChain = inputData.getGroupsPerChain();
			// Get the internal and public facing chain ids
			publicChainIds = decoderUtils.decodeChainList(inputData.getChainNameList());
			chainList = decoderUtils.decodeChainList(inputData.getChainIdList());
			spaceGroup = inputData.getSpaceGroup();
			unitCell = inputData.getUnitCell();
			bioAssembly  = inputData.getBioAssemblyList();
			interGroupBondIndices = decoderUtils.bytesToInts(inputData.getBondAtomList());
			interGroupBondOrders = decoderUtils.bytesToByteInts(inputData.getBondOrderList());
			mmtfVersion = inputData.getMmtfVersion();
			mmtfProducer = inputData.getMmtfProducer();
			entityList = inputData.getEntityList();
			pdbId = inputData.getPdbId();
			// Now get the header data
			rFree = inputData.getrFree();
			rWork = inputData.getrWork();
			resolution = inputData.getResolution();
			title = inputData.getTitle();
			experimentalMethods = inputData.getExperimentalMethods();
			

		}
		catch (IOException ioException){
			System.out.println("Error reading in byte arrays from message pack");
			ioException.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	/** The X coordinates */
	private float[] cartnX;

	/** The Y coordinates */
	private float[] cartnY;

	/** The Z coordinates */
	private float[] cartnZ;

	/** The X coordinates */
	private float[] bFactor;

	/** The Y coordinates */
	private float[] occupancy;
	
	/** The atom id. */
	private int[] atomId;

	/** The alt id. */
	private char[] altId;

	/** The ins code. */
	private char[] insertionCodeList;

	/** The group num. */
	private int[] groupNum;

	/** The group map. */
	private Map<Integer, PDBGroup> groupMap;

	/** The group list. */
	private int[] groupList;

	/** The sequence ids of the groups */
	private int[] seqResGroupList;

	/** The public facing chain ids*/
	private String[] publicChainIds;

	/** The number of chains per model*/
	private int[] chainsPerModel;

	/** The number of groups per (internal) chain*/
	private int[] groupsPerChain;

	/** The space group of the structure*/
	private String spaceGroup;

	/** The unit cell of the structure*/
	private float[] unitCell;

	/** The bioassembly information for the structure*/
	private List<BioAssemblyData> bioAssembly;

	/** The bond indices for bonds between groups*/
	private int[] interGroupBondIndices;

	/** The bond orders for bonds between groups*/
	private int[] interGroupBondOrders;

	/** The chosen list of chain ids */
	private String[] chainList;

	/** The mmtf version */
	private String mmtfVersion;

	/** The mmtf prodcuer */
	private String mmtfProducer;

	/** A list containing pdb group names for nucleic acids */
	List<String> nucAcidList = new ArrayList<>();

	/** The list of entities in this structure. */
	private Entity[] entityList;

	/** The PDB id	 */
	private String pdbId;
	
	/** The reported resolution of the dataset. */
	private float resolution;

	/** The reported R Free of the model. */
	private float rFree;
	
	/** The reported R Work of the model. */
	private float rWork;
	
	/** The title of the model. */
	private String title;

	/** The list of experimental methods. */
	private List<String> experimentalMethods;
	
	@Override
	public float[] getXcoords() {
		return cartnX;
	}

	@Override
	public float[] getYcoords() {
		return cartnY;
	}

	@Override
	public float[] getZcoords() {
		return cartnZ;
	}

	@Override
	public float[] getBfactors() {
		return bFactor;
	}

	@Override
	public float[] getOccupancies() {
		return occupancy;
	}

	@Override
	public String[] getChainList() {
		return this.chainList;
	}

	@Override
	public int[] getAtomIds() {
		return atomId;
	}

	@Override
	public char[] getAltLocIds() {
		return altId;
	}

	@Override
	public char[] getInsCodes() {
		return insertionCodeList;
	}

	@Override
	public int[] getResidueNums() {
		return groupNum;
	}

	@Override
	public Map<Integer, PDBGroup> getGroupMap() {
		return groupMap;
	}

	@Override
	public int[] getGroupIndices() {
		return groupList;
	}

	@Override
	public int[] getSeqResGroupIndices() {
		return seqResGroupList;
	}

	@Override
	public String[] getChainNames() {
		return publicChainIds;
	}

	@Override
	public int[] getChainsPerModel() {
		return chainsPerModel;
	}

	@Override
	public int[] getGroupsPerChain() {
		return groupsPerChain;
	}

	@Override
	public String getSpaceGroup() {
		return spaceGroup;
	}

	@Override
	public float[] getUnitCell() {
		return unitCell;
	}

	@Override
	public List<BioAssemblyData> getBioAssemblyList() {
		return bioAssembly;
	}

	@Override
	public int[] getInterGroupBondIndices() {
		return interGroupBondIndices;
	}

	@Override
	public int[] getInterGroupBondOrders() {
		return interGroupBondOrders;
	}

	@Override
	public String[] getChainIds() {
		return chainList;
	}

	@Override
	public String getMmtfVersion() {
		return mmtfVersion;
	}

	@Override
	public String getMmtfProducer() {
		return mmtfProducer;
	}

	@Override
	public Entity[] getEntityList() {
		return entityList;
	}

	@Override
	public String getPdbId() {
		return pdbId;
	}

	@Override
	public int getNumResidues() {
		return this.groupList.length;
	}

	@Override
	public int getNumChains() {
		return this.chainList.length;
	}
	
	@Override
	public int getNumModels() {	
		return this.chainsPerModel.length;
	}

	@Override
	public int getNumAtoms() {
		return this.cartnX.length;
	}

	@Override
	public float getRfree() {
		return rFree;
	}

	@Override
	public float getResolution() {
		return resolution;
	}

	@Override
	public float getRwork() {
		return rWork;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public List<String> getExperimentalMethods() {
		return experimentalMethods;
	}



}
