# capacitor-plugin-veriff

Capacitor plugin exposing Veriff SDK

## Install

```bash
npm install capacitor-plugin-veriff
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`start(...)`](#start)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(options: { value: string; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### start(...)

```typescript
start(sessionToken: string, configuration: { themeColor: string; }) => Promise<{ message: string; status: string; }>
```

| Param               | Type                                 |
| ------------------- | ------------------------------------ |
| **`sessionToken`**  | <code>string</code>                  |
| **`configuration`** | <code>{ themeColor: string; }</code> |

**Returns:** <code>Promise&lt;{ message: string; status: string; }&gt;</code>

--------------------

</docgen-api>
