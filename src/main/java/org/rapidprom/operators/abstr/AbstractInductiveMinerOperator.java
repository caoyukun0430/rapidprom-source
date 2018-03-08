package org.rapidprom.operators.abstr;

import java.util.List;

import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersEKS;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIM;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIMf;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIMflc;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.UndefinedParameterError;

public abstract class AbstractInductiveMinerOperator extends AbstractLabelAwareRapidProMDiscoveryOperator {

	public AbstractInductiveMinerOperator(OperatorDescription description) {
		super(description);
	}

	private static final String PARAMETER_1_KEY = "Variation",
			PARAMETER_1_DESCR = "The \"Inductive Miner\" variation is described in: "
					+ "http://dx.doi.org/10.1007/978-3-642-38697-8_17. \nThe \"Inductive Miner"
					+ " - Infrequent\" variation is described in: "
					+ "http://dx.doi.org/10.1007/978-3-319-06257-0_6. \nThe \"Inductive Miner"
					+ " - Incompleteness\" variation is described in:"
					+ "http://dx.doi.org/10.1007/978-3-319-07734-5_6. \nThe \"Inductive Miner"
					+ " - exhaustive K-successor\" variation applies a brute-force approach: "
					+ "in each recursion, it tries all 4*2^n cuts and measures which one "
					+ "fits the event log best. It measures this using the k-successor, "
					+ "which is a relation between pairs of activities, denoting how many "
					+ "events are in between them in any trace at minimum."
					+ "- Life cycle variation makes use of the 'start' and 'complete' transitions in the event log.",
			PARAMETER_2_KEY = "Noise Threshold",
			PARAMETER_2_DESCR = "This threshold represents the percentage of infrequent (noisy) "
					+ "traces that are filtered out. The remaining traces are used to discover a model. ";

	private static final String IM = "Inductive Miner", //
			IMi = "Inductive Miner - Infrequent", //
			IMin = "Inductive Miner - Incompleteness", //
			IMeks = "Inductive Miner - exhaustive K-successor", //
			IMflc = "Inductive Miner - Life cycle";

	@Override
	public List<ParameterType> getParameterTypes() {

		List<ParameterType> parameterTypes = super.getParameterTypes();

		ParameterTypeCategory parameter1 = new ParameterTypeCategory(PARAMETER_1_KEY, PARAMETER_1_DESCR,
				new String[] { IM, IMi, IMin, IMeks, IMflc }, 1);
		parameterTypes.add(parameter1);

		ParameterTypeDouble parameter2 = new ParameterTypeDouble(PARAMETER_2_KEY, PARAMETER_2_DESCR, 0, 1, 0.2);
		parameterTypes.add(parameter2);

		return parameterTypes;
	}

	protected MiningParameters getConfiguration() {
		MiningParameters miningParameters = null;
		try {
			if (getParameterAsString(PARAMETER_1_KEY).equals(IM))
				miningParameters = new MiningParametersIM();
			else if (getParameterAsString(PARAMETER_1_KEY).equals(IMi))
				miningParameters = new MiningParametersIMf();
			else if (getParameterAsString(PARAMETER_1_KEY).equals(IMin))
				miningParameters = new MiningParametersIMf();
			else if (getParameterAsString(PARAMETER_1_KEY).equals(IMeks))
				miningParameters = new MiningParametersEKS();
			else if (getParameterAsString(PARAMETER_1_KEY).equals(IMflc))
				miningParameters = new MiningParametersIMflc();
			else
				throw new IllegalArgumentException(
						"Unknown inductive miner type " + getParameterAsString(PARAMETER_1_KEY));

			miningParameters.setNoiseThreshold((float) getParameterAsDouble(PARAMETER_2_KEY));
			miningParameters.setClassifier(getXEventClassifier());
		} catch (UndefinedParameterError e) {
			e.printStackTrace();
		}
		return miningParameters;
	}
}
