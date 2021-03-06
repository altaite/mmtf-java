package org.rcsb.mmtf.decoder;

import org.rcsb.mmtf.api.StructureAdapterInterface;

/**
 * A dummy implementation of the {@link StructureAdapterInterface}.
 * @author Anthony Bradley
 *
 */
public class DummyTransferImpl implements StructureAdapterInterface {

	@Override
	public void initStructure(int totalNumBonds, int totalNumAtoms, int totalNumGroups, int totalNumChains,
			int totalNumModels, String structureId) {
		
	}

	@Override
	public void finalizeStructure() {
		
	}

	@Override
	public void setModelInfo(int modelId, int chainCount) {
		
	}

	@Override
	public void setChainInfo(String chainId, String chainName, int groupCount) {
		
	}

	@Override
	public void setEntityInfo(int[] chainIndices, String sequence, String description, String title) {
		
	}

	@Override
	public void setGroupInfo(String groupName, int groupNumber, char insertionCode, String groupType, int atomCount,
			int boundCount, char singleLetterCode, int sequenceIndex, int secondaryStructureType) {
		
	}

	@Override
	public void setAtomInfo(String atomName, int serialNumber, char alternativeLocationId, float x, float y, float z,
			float occupancy, float temperatureFactor, String element, int charge) {
		
	}

	@Override
	public void setBioAssemblyTrans(int bioAssemblyIndex, int[] inputChainIndices, double[] inputTransform, String name) {
		
	}

	@Override
	public void setXtalInfo(String spaceGroup, float[] unitCell, double[][] ncsOperatorList) {
		
	}

	@Override
	public void setGroupBond(int atomIndexOne, int atomIndexTwo, int bondOrder) {
		
	}

	@Override
	public void setInterGroupBond(int atomIndexOne, int atomIndexTwo, int bondOrder) {
		
	}



	@Override
	public void setHeaderInfo(float rFree, float rWork, float resolution, String title, String depositionDate,
			String releaseDate, String[] experimnetalMethods) {
		
	}

}
