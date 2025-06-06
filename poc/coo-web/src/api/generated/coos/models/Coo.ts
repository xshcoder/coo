/* tslint:disable */
/* eslint-disable */
/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { mapValues } from '../runtime';
/**
 * 
 * @export
 * @interface Coo
 */
export interface Coo {
    /**
     * 
     * @type {string}
     * @memberof Coo
     */
    id?: string;
    /**
     * 
     * @type {string}
     * @memberof Coo
     */
    userId?: string;
    /**
     * 
     * @type {string}
     * @memberof Coo
     */
    content?: string;
    /**
     * 
     * @type {Date}
     * @memberof Coo
     */
    createdAt?: Date;
}

/**
 * Check if a given object implements the Coo interface.
 */
export function instanceOfCoo(value: object): value is Coo {
    return true;
}

export function CooFromJSON(json: any): Coo {
    return CooFromJSONTyped(json, false);
}

export function CooFromJSONTyped(json: any, ignoreDiscriminator: boolean): Coo {
    if (json == null) {
        return json;
    }
    return {
        
        'id': json['id'] == null ? undefined : json['id'],
        'userId': json['userId'] == null ? undefined : json['userId'],
        'content': json['content'] == null ? undefined : json['content'],
        'createdAt': json['createdAt'] == null ? undefined : (new Date(json['createdAt'])),
    };
}

export function CooToJSON(json: any): Coo {
    return CooToJSONTyped(json, false);
}

export function CooToJSONTyped(value?: Coo | null, ignoreDiscriminator: boolean = false): any {
    if (value == null) {
        return value;
    }

    return {
        
        'id': value['id'],
        'userId': value['userId'],
        'content': value['content'],
        'createdAt': value['createdAt'] == null ? undefined : ((value['createdAt']).toISOString()),
    };
}

