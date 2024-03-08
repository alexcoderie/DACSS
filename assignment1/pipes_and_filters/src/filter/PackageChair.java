package filter;

import product.Chair;

public class PackageChair implements Filter<Chair> {
    @Override
    public Chair process(Chair chair) {
        chair.packageChair();
        return chair;
    }
}
