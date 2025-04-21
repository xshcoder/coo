import { CooControllerApi, Configuration } from '../generated/coos';

class CooApiClient {
    private static instance: CooControllerApi | null = null;
    
    private constructor() {}
    
    public static getInstance(): CooControllerApi {
        if (!this.instance) {
            const config = new Configuration({
                basePath: import.meta.env.VITE_API_BASE_URL || window.location.origin,
                credentials: 'include',
            });
            this.instance = new CooControllerApi(config);
        }
        return this.instance;
    }
}

export const cooApi = CooApiClient.getInstance();