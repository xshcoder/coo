import { SearchControllerApi, Configuration } from '../generated/search';

class SearchApiClient {
    private static instance: SearchControllerApi | null = null;
    
    private constructor() {}
    
    public static getInstance(): SearchControllerApi {
        if (!this.instance) {
            const config = new Configuration({
                basePath: import.meta.env.VITE_API_BASE_URL || window.location.origin,
                credentials: 'include',
            });
            this.instance = new SearchControllerApi(config);
        }
        return this.instance;
    }
}

export const searchApi = SearchApiClient.getInstance();