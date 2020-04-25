import { Injectable } from '@angular/core';
import { Product } from '../model/product';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { PRODUCTS } from '../data/products';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {


  constructor(
    private http: HttpClient
  ) { }

  getProducts(): Observable<Product[]> {
    return of (PRODUCTS);
    // return this.http.get<Product[]>(this.productsUrl)
    //   .pipe(
    //     tap(_ => this.log('fetched products')),
    //     catchError(this.handleError<Product[]>('getProducts', []))
    //   );
  }

  getProduct(id: number): Observable<Product>{
    return of (PRODUCTS.find(x => x.id === id));
  }

  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a HeroService message with the MessageService */
  private log(message: string) {
  }
}
