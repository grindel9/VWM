import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Brand, Country } from '../model/api/dropdown';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Product } from '../model/product';
import { Filtermodel } from './filterable-list.component';

@Injectable({
  providedIn: 'root'
})


export class FilterableListService {

  private REST_API_SERVER = "http://localhost:8080/java-api";

  constructor(private http:HttpClient) { }

  getBrands(): Observable<Brand[]> {
    return this.http.get<Brand[]>(this.REST_API_SERVER+"/brand/");
  }

  getCountries(): Observable<Country[]> {
    return this.http.get<Country[]>(this.REST_API_SERVER+"/country/");
  }

  getModels(): Observable<string[]> {
    return this.http.get<string[]>(this.REST_API_SERVER+"/model/");
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.REST_API_SERVER+"/computer/");
  }

  postProducts(req:Filtermodel): Observable<any> {
    // console.log("JSON request:");
    // const headers = {'content-type':'application/json'}
    const body = JSON.stringify(req);
    console.log(body);
    return this.http.post<any>(this.REST_API_SERVER+"/computer/", body);
  }
}