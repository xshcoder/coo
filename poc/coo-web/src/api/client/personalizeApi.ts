import { PersonalizeControllerApi, Configuration } from '../generated/personalize';

class PersonalizeApiClient {
    private static instance: PersonalizeControllerApi | null = null;
    
    private constructor() {}
    
    public static getInstance(): PersonalizeControllerApi {
        if (!this.instance) {
            const config = new Configuration({
                basePath: import.meta.env.VITE_API_BASE_URL || window.location.origin,
                credentials: 'include',
            });
            this.instance = new PersonalizeControllerApi(config);
        }
        return this.instance;
    }
}

export const personalizeApi = PersonalizeApiClient.getInstance();