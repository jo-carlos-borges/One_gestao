import StringMask from 'string-mask';

export function maskPhone(value) {
   let cleanValue = value.replace(/\D/g, ''); // Remove todos os caracteres não numéricos

   if (value.length > 11) {
      cleanValue = cleanValue.slice(0, 11); // Limita a entrada a 11 dígitos
   }

   const phoneMask = new StringMask('(00) 00000-0000');

   // Aplica a máscara ao valor limpo
   return phoneMask.apply(cleanValue);
}


export function hasSpecialCharacters(str) {
   return /[^\w\s]/.test(str);
}
